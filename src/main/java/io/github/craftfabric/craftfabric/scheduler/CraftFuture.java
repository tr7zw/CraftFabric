package io.github.craftfabric.craftfabric.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.bukkit.plugin.Plugin;

class CraftFuture<T> extends CraftTask implements Future<T> {
   private final Callable<T> callable;
   private T value;
   private Exception exception = null;

   CraftFuture(Callable<T> callable, Plugin plugin, int id) {
      super(plugin, (Object)null, id, -1L);
      this.callable = callable;
   }

   public synchronized boolean cancel(boolean mayInterruptIfRunning) {
      if (this.getPeriod() != -1L) {
         return false;
      } else {
         this.setPeriod(-2L);
         return true;
      }
   }

   public boolean isDone() {
      long period = this.getPeriod();
      return period != -1L && period != -3L;
   }

   public T get() throws CancellationException, InterruptedException, ExecutionException {
      try {
         return this.get(0L, TimeUnit.MILLISECONDS);
      } catch (TimeoutException var2) {
         throw new Error(var2);
      }
   }

   public synchronized T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
      timeout = unit.toMillis(timeout);
      long period = this.getPeriod();
      long timestamp = timeout > 0L ? System.currentTimeMillis() : 0L;

      while(period == -1L || period == -3L) {
         this.wait(timeout);
         period = this.getPeriod();
         if (period != -1L && period != -3L) {
            break;
         }

         if (timeout != 0L) {
            timeout += timestamp - (timestamp = System.currentTimeMillis());
            if (timeout <= 0L) {
               throw new TimeoutException();
            }
         }
      }

      if (period == -2L) {
         throw new CancellationException();
      } else if (period == -4L) {
         if (this.exception == null) {
            return this.value;
         } else {
            throw new ExecutionException(this.exception);
         }
      } else {
         throw new IllegalStateException("Expected -1 to -4, got " + period);
      }
   }

   public void run() {
      synchronized(this) {
         if (this.getPeriod() == -2L) {
            return;
         }

         this.setPeriod(-3L);
      }

      try {
         this.value = (T) this.callable.call();
      } catch (Exception var11) {
         this.exception = var11;
      } finally {
         synchronized(this) {
            this.setPeriod(-4L);
            this.notifyAll();
         }
      }

   }

   synchronized boolean cancel0() {
      if (this.getPeriod() != -1L) {
         return false;
      } else {
         this.setPeriod(-2L);
         this.notifyAll();
         return true;
      }
   }
}
