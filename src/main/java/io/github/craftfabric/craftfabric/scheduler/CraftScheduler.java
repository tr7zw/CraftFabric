package io.github.craftfabric.craftfabric.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

public class CraftScheduler implements BukkitScheduler {
	private final AtomicInteger ids = new AtomicInteger(1);
	private volatile CraftTask head = new CraftTask();
	private final AtomicReference<CraftTask> tail = new AtomicReference(this.head);
	private final PriorityQueue<CraftTask> pending = new PriorityQueue(10, new Comparator<CraftTask>() {
		public int compare(CraftTask o1, CraftTask o2) {
			int value = Long.compare(o1.getNextRun(), o2.getNextRun());

			return value != 0 ? value : Integer.compare(o1.getTaskId(), o2.getTaskId());
		}
	});
	private final List<CraftTask> temp = new ArrayList();
	private final ConcurrentHashMap<Integer, CraftTask> runners = new ConcurrentHashMap();
	private volatile CraftTask currentTask = null;
	private volatile int currentTick = -1;
	private final Executor executor = Executors
			.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("Craft Scheduler Thread - %d").build());
	private static final int RECENT_TICKS = 30;

	  public int scheduleSyncDelayedTask(Plugin plugin, Runnable task) {
	      return this.scheduleSyncDelayedTask(plugin, task, 0L);
	   }

	   public BukkitTask runTask(Plugin plugin, Runnable runnable) {
	      return this.runTaskLater(plugin, runnable, 0L);
	   }

	   public void runTask(Plugin plugin, Consumer task) throws IllegalArgumentException {
	      this.runTaskLater(plugin, task, 0L);
	   }

	   /** @deprecated */
	   @Deprecated
	   public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task) {
	      return this.scheduleAsyncDelayedTask(plugin, task, 0L);
	   }

	   public BukkitTask runTaskAsynchronously(Plugin plugin, Runnable runnable) {
	      return this.runTaskLaterAsynchronously(plugin, runnable, 0L);
	   }

	   public void runTaskAsynchronously(Plugin plugin, Consumer task) throws IllegalArgumentException {
	      this.runTaskLaterAsynchronously(plugin, task, 0L);
	   }

	   public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay) {
	      return this.scheduleSyncRepeatingTask(plugin, task, delay, -1L);
	   }

	   public BukkitTask runTaskLater(Plugin plugin, Runnable runnable, long delay) {
	      return this.runTaskTimer(plugin, runnable, delay, -1L);
	   }

	   public void runTaskLater(Plugin plugin, Consumer task, long delay) throws IllegalArgumentException {
	      this.runTaskTimer(plugin, task, delay, -1L);
	   }

	   /** @deprecated */
	   @Deprecated
	   public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay) {
	      return this.scheduleAsyncRepeatingTask(plugin, task, delay, -1L);
	   }

	   public BukkitTask runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long delay) {
	      return this.runTaskTimerAsynchronously(plugin, runnable, delay, -1L);
	   }

	   public void runTaskLaterAsynchronously(Plugin plugin, Consumer task, long delay) throws IllegalArgumentException {
	      this.runTaskTimerAsynchronously(plugin, task, delay, -1L);
	   }

	   public void runTaskTimerAsynchronously(Plugin plugin, Consumer task, long delay, long period) throws IllegalArgumentException {
	      this.runTaskTimerAsynchronously(plugin, (Object)task, delay, -1L);
	   }

	   public int scheduleSyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long period) {
	      return this.runTaskTimer(plugin, runnable, delay, period).getTaskId();
	   }

	   public BukkitTask runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period) {
	      return this.runTaskTimer(plugin, (Object)runnable, delay, period);
	   }

	   public void runTaskTimer(Plugin plugin, Consumer task, long delay, long period) throws IllegalArgumentException {
	      this.runTaskTimer(plugin, (Object)task, delay, period);
	   }

	public BukkitTask runTaskTimer(Plugin plugin, Object runnable, long delay, long period) {
		validate(plugin, runnable);
		if (delay < 0L) {
			delay = 0L;
		}
		if (period == 0L) {
			period = 1L;
		} else if (period < -1L) {
			period = -1L;
		}
		return handle(new CraftTask(plugin, runnable, nextId(), period), delay);
	}

	@Deprecated
	public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long period) {
		return runTaskTimerAsynchronously(plugin, runnable, delay, period).getTaskId();
	}

	public BukkitTask runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long delay, long period) {
		return runTaskTimerAsynchronously(plugin, (Object)runnable, delay, period);
	}

	public BukkitTask runTaskTimerAsynchronously(Plugin plugin, Object runnable, long delay, long period) {
		validate(plugin, runnable);
		if (delay < 0L) {
			delay = 0L;
		}
		if (period == 0L) {
			period = 1L;
		} else if (period < -1L) {
			period = -1L;
		}
		return handle(new CraftAsyncTask(this.runners, plugin, runnable, nextId(), period), delay);
	}

	public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> task) {
		validate(plugin, task);
		CraftFuture<T> future = new CraftFuture(task, plugin, nextId());
		handle(future, 0L);
		return future;
	}

	public void cancelTask(final int taskId) {
		if (taskId <= 0) {
			return;
		}
		CraftTask task = (CraftTask) this.runners.get(Integer.valueOf(taskId));
		if (task != null) {
			task.cancel0();
		}
		task = new CraftTask(new Runnable() {
			public void run() {
				if (!check(CraftScheduler.this.temp)) {
					check(CraftScheduler.this.pending);
				}
			}

			private boolean check(Iterable<CraftTask> collection) {
				Iterator<CraftTask> tasks = collection.iterator();
				while (tasks.hasNext()) {
					CraftTask task = (CraftTask) tasks.next();
					if (task.getTaskId() == taskId) {
						task.cancel0();
						tasks.remove();
						if (task.isSync()) {
							CraftScheduler.this.runners.remove(Integer.valueOf(taskId));
						}
						return true;
					}
				}
				return false;
			}
		});
		handle(task, 0L);
		for (CraftTask taskPending = this.head.getNext(); taskPending != null; taskPending = taskPending.getNext()) {
			if (taskPending == task) {
				return;
			}
			if (taskPending.getTaskId() == taskId) {
				taskPending.cancel0();
			}
		}
	}

	public void cancelTasks(final Plugin plugin) {
		Validate.notNull(plugin, "Cannot cancel tasks of null plugin");
		CraftTask task = new CraftTask(new Runnable() {
			public void run() {
				check(CraftScheduler.this.pending);
				check(CraftScheduler.this.temp);
			}

			void check(Iterable<CraftTask> collection) {
				Iterator<CraftTask> tasks = collection.iterator();
				while (tasks.hasNext()) {
					CraftTask task = (CraftTask) tasks.next();
					if (task.getOwner().equals(plugin)) {
						task.cancel0();
						tasks.remove();
						if (task.isSync()) {
							CraftScheduler.this.runners.remove(Integer.valueOf(task.getTaskId()));
						}
					}
				}
			}
		});
		handle(task, 0L);
		for (CraftTask taskPending = this.head.getNext(); taskPending != null; taskPending = taskPending.getNext()) {
			if (taskPending == task) {
				break;
			}
			if ((taskPending.getTaskId() != -1) && (taskPending.getOwner().equals(plugin))) {
				taskPending.cancel0();
			}
		}
		for (CraftTask runner : this.runners.values()) {
			if (runner.getOwner().equals(plugin)) {
				runner.cancel0();
			}
		}
	}

	public boolean isCurrentlyRunning(int taskId) {
		CraftTask task = (CraftTask) this.runners.get(Integer.valueOf(taskId));
		if (task == null) {
			return false;
		}
		if (task.isSync()) {
			return task == this.currentTask;
		}
		CraftAsyncTask asyncTask = (CraftAsyncTask) task;
		synchronized (asyncTask.getWorkers()) {
			return !asyncTask.getWorkers().isEmpty();
		}
	}

	public boolean isQueued(int taskId) {
		if (taskId <= 0) {
			return false;
		}
		for (CraftTask task = this.head.getNext(); task != null; task = task.getNext()) {
			if (task.getTaskId() == taskId) {
				return task.getPeriod() >= -1L;
			}
		}
		CraftTask task = (CraftTask) this.runners.get(Integer.valueOf(taskId));
		return (task != null) && (task.getPeriod() >= -1L);
	}

	public List<BukkitWorker> getActiveWorkers() {
		ArrayList<BukkitWorker> workers = new ArrayList();
		for (CraftTask taskObj : this.runners.values()) {
			if (!taskObj.isSync()) {
				CraftAsyncTask task = (CraftAsyncTask) taskObj;
				synchronized (task.getWorkers()) {
					workers.addAll(task.getWorkers());
				}
			}
		}
		return workers;
	}

	public List<BukkitTask> getPendingTasks() {
		ArrayList<CraftTask> truePending = new ArrayList();
		for (CraftTask task = this.head.getNext(); task != null; task = task.getNext()) {
			if (task.getTaskId() != -1) {
				truePending.add(task);
			}
		}
		ArrayList<BukkitTask> pending = new ArrayList();
		for (CraftTask task : this.runners.values()) {
			if (task.getPeriod() >= -1L) {
				pending.add(task);
			}
		}
		for (CraftTask task : truePending) {
			if ((task.getPeriod() >= -1L) && (!pending.contains(task))) {
				pending.add(task);
			}
		}
		return pending;
	}

	public void mainThreadHeartbeat(int currentTick) {
		this.currentTick = currentTick;
		List temp = this.temp;
		this.parsePending();

		while (this.isReady(currentTick)) {
			CraftTask task = (CraftTask) this.pending.remove();
			if (task.getPeriod() < -1L) {
				if (task.isSync()) {
					this.runners.remove(task.getTaskId(), task);
				}

				this.parsePending();
			} else {
				if (task.isSync()) {
					this.currentTask = task;

					try {
						task.run();
					} catch (Throwable var8) {
						task.getOwner().getLogger().log(Level.WARNING,
								String.format("Task #%s for %s generated an exception", task.getTaskId(),
										task.getOwner().getDescription().getFullName()),
								var8);
					} finally {
						this.currentTask = null;
					}

					this.parsePending();
				} else {
					this.executor.execute(task);
				}

				long period = task.getPeriod();
				if (period > 0L) {
					task.setNextRun((long) currentTick + period);
					temp.add(task);
				} else if (task.isSync()) {
					this.runners.remove(task.getTaskId());
				}
			}
		}

		this.pending.addAll(temp);
		temp.clear();
	}

	private void addTask(CraftTask task) {
		AtomicReference<CraftTask> tail = this.tail;
		CraftTask tailTask = (CraftTask) tail.get();
		while (!tail.compareAndSet(tailTask, task)) {
			tailTask = (CraftTask) tail.get();
		}
		tailTask.setNext(task);
	}

	private CraftTask handle(CraftTask task, long delay) {
		task.setNextRun(this.currentTick + delay);
		addTask(task);
		return task;
	}

	private static void validate(Plugin plugin, Object task) {
		Validate.notNull(plugin, "Plugin cannot be null");
		Validate.notNull(task, "Task cannot be null");
		Validate.isTrue(((task instanceof Runnable)) || ((task instanceof Consumer)) || ((task instanceof Callable)),
				"Task must be Runnable, Consumer, or Callable");
		if (!plugin.isEnabled()) {
			throw new IllegalPluginAccessException("Plugin attempted to register task while disabled");
		}
	}

	private int nextId() {
		return this.ids.incrementAndGet();
	}

	private void parsePending() {
		CraftTask head = this.head;
		CraftTask task = head.getNext();
		CraftTask lastTask = head;
		for (; task != null; task = (lastTask = task).getNext()) {
			if (task.getTaskId() == -1) {
				task.run();
			} else if (task.getPeriod() >= -1L) {
				this.pending.add(task);
				this.runners.put(Integer.valueOf(task.getTaskId()), task);
			}
		}
		for (task = head; task != lastTask; task = head) {
			head = task.getNext();
			task.setNext(null);
		}
		this.head = lastTask;
	}

	private boolean isReady(int currentTick) {
		return (!this.pending.isEmpty()) && (((CraftTask) this.pending.peek()).getNextRun() <= currentTick);
	}

	public String toString() {
		int debugTick = this.currentTick;
		StringBuilder string = new StringBuilder("Recent tasks from ").append(debugTick - RECENT_TICKS).append('-')
				.append(debugTick).append('{').append('}');
		return string.toString();
	}

	@Deprecated
	public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task, long delay) {
		throw new UnsupportedOperationException("Use BukkitRunnable#runTaskLater(Plugin, long)");
	}

	@Deprecated
	public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task) {
		throw new UnsupportedOperationException("Use BukkitRunnable#runTask(Plugin)");
	}

	@Deprecated
	public int scheduleSyncRepeatingTask(Plugin plugin, BukkitRunnable task, long delay, long period) {
		throw new UnsupportedOperationException("Use BukkitRunnable#runTaskTimer(Plugin, long, long)");
	}

	@Deprecated
	public BukkitTask runTask(Plugin plugin, BukkitRunnable task) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Use BukkitRunnable#runTask(Plugin)");
	}

	@Deprecated
	public BukkitTask runTaskAsynchronously(Plugin plugin, BukkitRunnable task) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Use BukkitRunnable#runTaskAsynchronously(Plugin)");
	}

	@Deprecated
	public BukkitTask runTaskLater(Plugin plugin, BukkitRunnable task, long delay) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Use BukkitRunnable#runTaskLater(Plugin, long)");
	}

	@Deprecated
	public BukkitTask runTaskLaterAsynchronously(Plugin plugin, BukkitRunnable task, long delay)
			throws IllegalArgumentException {
		throw new UnsupportedOperationException("Use BukkitRunnable#runTaskLaterAsynchronously(Plugin, long)");
	}

	@Deprecated
	public BukkitTask runTaskTimer(Plugin plugin, BukkitRunnable task, long delay, long period)
			throws IllegalArgumentException {
		throw new UnsupportedOperationException("Use BukkitRunnable#runTaskTimer(Plugin, long, long)");
	}

	@Deprecated
	public BukkitTask runTaskTimerAsynchronously(Plugin plugin, BukkitRunnable task, long delay, long period)
			throws IllegalArgumentException {
		throw new UnsupportedOperationException("Use BukkitRunnable#runTaskTimerAsynchronously(Plugin, long, long)");
	}
}
