from locust import FastHttpUser, between, task

class WebsiteUser(FastHttpUser):
    wait_time = between(1, 5)
    
    @task
    def index(self):
        self.client.get("/")