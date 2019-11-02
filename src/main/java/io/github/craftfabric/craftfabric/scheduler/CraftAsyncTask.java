package io.github.craftfabric.craftfabric.scheduler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitWorker;

class CraftAsyncTask extends CraftTask {
	private final LinkedList workers = new LinkedList();
	private final Map runners;

	CraftAsyncTask(Map runners, Plugin plugin, Object task, int id, long delay) {
		super(plugin, task, id, delay);
		this.runners = runners;
	}

	class CraftAsyncTask$1 implements BukkitWorker {
		final CraftAsyncTask parent;
		private final Thread thread;

		CraftAsyncTask$1(CraftAsyncTask var1, Thread var2) {
			this.parent = var1;
			this.thread = var2;
		}

		public Thread getThread() {
			return this.thread;
		}

		public int getTaskId() {
			return this.parent.getTaskId();
		}

		public Plugin getOwner() {
			return this.parent.getOwner();
		}
	}

	public boolean isSync() {
		return false;
	}

	public void run() {
		Thread thread = Thread.currentThread();
		LinkedList var2 = this.workers;
		synchronized (this.workers) {
			if (this.getPeriod() == -2L) {
				return;
			}

			this.workers.add(new CraftAsyncTask$1(this, thread));
		}

		Throwable thrown = null;

		try {
			super.run();
		} catch (Throwable var45) {
			thrown = var45;
			this.getOwner().getLogger().log(Level.WARNING,
					String.format("Plugin %s generated an exception while executing task %s",
							this.getOwner().getDescription().getFullName(), this.getTaskId()),
					var45);
		} finally {
			LinkedList var5 = this.workers;
			synchronized (this.workers) {
				try {
					Iterator workers = this.workers.iterator();
					boolean removed = false;

					while (workers.hasNext()) {
						if (((BukkitWorker) workers.next()).getThread() == thread) {
							workers.remove();
							removed = true;
							break;
						}
					}

					if (!removed) {
						throw new IllegalStateException(String.format("Unable to remove worker %s on task %s for %s",
								thread.getName(), this.getTaskId(), this.getOwner().getDescription().getFullName()),
								thrown);
					}
				} finally {
					if (this.getPeriod() < 0L && this.workers.isEmpty()) {
						this.runners.remove(this.getTaskId());
					}

				}

			}
		}

	}

	LinkedList getWorkers() {
		return this.workers;
	}

	boolean cancel0() {
		LinkedList var1 = this.workers;
		synchronized (this.workers) {
			this.setPeriod(-2L);
			if (this.workers.isEmpty()) {
				this.runners.remove(this.getTaskId());
			}

			return true;
		}
	}
}
