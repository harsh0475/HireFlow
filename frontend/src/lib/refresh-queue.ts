type Subscriber = (token: string | null) => void;

class RefreshQueue {
  private isRefreshing = false;

  private subscribers: Subscriber[] = [];

  startRefresh() {
    this.isRefreshing = true;
  }

  finishRefresh(token: string) {
    this.isRefreshing = false;

    this.subscribers.forEach((callback) => callback(token));

    this.subscribers = [];
  }

  failRefresh() {
    this.isRefreshing = false;

    this.subscribers.forEach((callback) => callback(null));

    this.subscribers = [];
  }

  subscribe(callback: Subscriber) {
    this.subscribers.push(callback);
  }

  get refreshing() {
    return this.isRefreshing;
  }
}

export const refreshQueue = new RefreshQueue();