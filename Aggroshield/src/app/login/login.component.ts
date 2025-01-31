import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  role: string = '';
  errorMessage: string | null = null;

  constructor(private router: Router, private http: HttpClient) {}

  // Handle login submission
  onSubmit() {
    if (!this.role) {
      this.errorMessage = 'Please select a role (User or Admin).';
      return;
    }

    // Create the payload
    const payload = {
      username: this.username,
      password: this.password,
      role: this.role,
    };

    // Post the data
    this.http.post<any>('http://localhost:8090/api/auth/login', payload).subscribe({
      next: (response) => {
        console.log('Login successful:', response);

        // Extract and store the token
        const token = response.data.token; // Adjust based on your API's response structure
        if (token) {
          localStorage.setItem('authToken', token);
          console.log('Token stored in localStorage:', token);
          if(this.role=="Admin"){
            this.router.navigate(["/drivers"])
          }
          else{
            this.router.navigate(["/dashboard"])
          }
        }

        // Navigate to the dashboard or another page
        
      },
      error: (error) => {
        console.error('Login failed:', error);
        this.errorMessage = 'Invalid login credentials. Please try again.';
      },
    });

    // Reset error message
    this.errorMessage = '';
  }
}
