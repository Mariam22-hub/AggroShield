import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss'],
})
export class LayoutComponent implements OnInit {
  activeLink: string = 'dashboard';
  fullName: string = 'User'; // Placeholder for user's full name
  userId!: number; // ID of the logged-in user
  alerts: any[] = []; // List of alerts

  // Variables for PDF Report Generation
  selectedReportType: string = 'week'; // Default to weekly report
  loading: boolean = false;
  error: string | null = null;
  success: boolean = false;

  private reportBaseUrl = 'http://localhost:8086/api/reports/generate';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.fetchUserDetails();
    this.fetchAlerts();
  }

  // Fetch user details from the API
  fetchUserDetails(): void {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    this.http.get<any>('http://localhost:8090/users/me', { headers }).subscribe(
      (response) => {
        this.fullName = response.fullName;
        this.userId = response.id; // Assuming the response includes the user ID
      },
      (error) => {
        console.error('Error fetching user details:', error);
      }
    );
  }

  // Fetch alerts for the user
  fetchAlerts(): void {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    this.http.get<any[]>('http://localhost:8090/api/alerts/user', { headers }).subscribe(
      (alerts) => {
        this.alerts = alerts;
      },
      (error) => {
        console.error('Error fetching alerts:', error);
      }
    );
  }

  // Generate PDF Report
  generateReport(): void {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    const apiUrl = `${this.reportBaseUrl}?groupBy=${this.selectedReportType}`;

    this.loading = true;
    this.error = null;
    this.success = false;

    this.http
      .post(apiUrl, {}, { headers, responseType: 'blob' })
      .subscribe({
        next: (response) => {
          this.downloadFile(response, `report_${this.selectedReportType}.pdf`);
          this.success = true;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Failed to generate the report. Please try again.';
          console.error('API Error:', err);
          this.loading = false;
        },
      });
  }

  // Download the generated file
  downloadFile(blob: Blob, fileName: string): void {
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = fileName;
    link.click();
    window.URL.revokeObjectURL(link.href);
  }

  // Navigate to the driver's profile page
  navigateToUserProfile(): void {
    if (this.userId) {
      this.router.navigate([`/driver-profile/${this.userId}`]);
    } else {
      console.error('User ID is not available.');
    }
  }

  // Set the active sidebar link
  setActive(link: string): void {
    this.activeLink = link;
  }

  // Logout method
  logout(): void {
    localStorage.removeItem('authToken');
    this.router.navigate(["/login"])
    console.log('User logged out!');
  }
}
