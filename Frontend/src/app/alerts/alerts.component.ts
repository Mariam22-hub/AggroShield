import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-alerts',
  templateUrl: './alerts.component.html',
  styleUrls: ['./alerts.component.scss'],
})
export class AlertComponent implements OnInit {
  alerts: any[] = [];
  activeLink="alerts";
  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.fetchAlerts();
  }

  // Fetch alerts for the driver
  fetchAlerts(): void {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    this.http
      .get<any[]>(`http://localhost:8090/api/alerts/admin/users`, { headers })
      .subscribe(
        (alerts) => {
          this.alerts = alerts;
          console.log(alerts);
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
