from locust import FastHttpUser, between, task

class WebsiteUser(FastHttpUser):
    wait_time = between(5, 15)
    
    @task
    def index(self):
        self.client.get("/")