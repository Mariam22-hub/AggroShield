import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent {
  // Form fields
  firstName: string = '';
  lastName: string = '';
  username: string = '';
  password: string = '';
  confirmPassword: string = '';
  phoneNumber: string = '';
  carModel: string = '';
  vehicleNumber: string = '';
  email: string = '';
  errorMessage: string | null = null;

  constructor(private router: Router, private http: HttpClient) {}

  // Handle signup form submission
  onSubmit() {
    // Validate passwords match
    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }

    // Create payload
    const payload = {
      username: this.username,
      password: this.password,
      firstName: this.firstName,
      lastName: this.lastName,
      phoneNumber: this.phoneNumber,
      carModel: this.carModel,
      vehicleNumber: this.vehicleNumber,
      email: this.email
    };

    // Make the HTTP POST request
    this.http.post('http://localhost:8090/api/auth/register', payload).subscribe({
      next: (response: any) => {
        console.log('Signup successful:', response);

        // Save the token in localStorage
        if (response?.data?.token) {
          localStorage.setItem('authToken', response.data.token);
          console.log('Token saved to localStorage:', response.data.token);
        }

        // Navigate to the login page on success
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Signup failed:', error);
        this.errorMessage = 'An error occurred during signup. Please try again.';
      }
    });
  }

  // Navigate to login page
  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
