import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-driver-profile',
  templateUrl: './driver-profile.component.html',
  styleUrls: ['./driver-profile.component.scss'],
})
export class DriverProfileComponent implements OnInit {
  driverDetails: any = null; // To hold driver details
  alertsCount = 0;
  alerts: any[] = [];
  activeLink="driver-profile"
  constructor(
    private route: ActivatedRoute,
    private http: HttpClient, private router: Router
  ) {}

  ngOnInit(): void {
    // Get the driver ID from the route
    const driverId = this.route.snapshot.paramMap.get('id');
    if (driverId) {
      this.fetchDriverDetails(driverId);
      
    }
  }

  // Fetch driver details from the API
  fetchDriverDetails(driverId: string): void {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    this.http
      .get<any>(`http://localhost:8090/users/${driverId}`, { headers })
      .subscribe(
        (data) => {
          this.driverDetails = data;
          console.log(this.driverDetails)
          this.fetchAlerts(this.driverDetails.email);
        },
        (error) => {
          this.fetchSelfDriverDetails();
        }
      );
      
  }
  fetchSelfDriverDetails(): void {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    this.http
      .get<any>(`http://localhost:8090/users/me`, { headers })
      .subscribe(
        (data) => {
          this.driverDetails = data;
          this.driverDetails.firstName= data.fullName
          this.driverDetails.role= "User"
          this.fetchAlerts(this.driverDetails.email);
        },
        (error) => {
          console.error('Error fetching driver details:', error);
        }
      );
      
  }

  // Fetch alerts for the driver
  fetchAlerts(email: string): void {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    this.http
      .get<any[]>(`http://localhost:8090/api/alerts/user/${email}`, { headers })
      .subscribe(
        (alerts) => {
          this.alerts = alerts;
          this.alertsCount = alerts.length;
        },
        (error) => {
          console.error('Error fetching alerts:', error);
        }
      );
  }

  // Logout method
  logout(): void {
    localStorage.removeItem('authToken');
    this.router.navigate(["/login"]);
    console.log('User logged out!');
  }
  setActive(link: string): void {
    this.activeLink = link;
  }
}
