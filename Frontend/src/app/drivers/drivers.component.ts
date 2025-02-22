import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-drivers',
  templateUrl: './drivers.component.html',
  styleUrls: ['./drivers.component.scss'],
})
export class DriversComponent implements OnInit {
  displayedColumns: string[] = ['fullName', 'carModel', 'carNumber'];
  dataSource = new MatTableDataSource<any>([]);
  activeLink = 'drivers';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.fetchUsers();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  fetchUsers() {
    const token = localStorage.getItem('authToken');
    if (!token) {
      console.error('No token found in localStorage.');
      return;
    }

    const headers = {
      Authorization: `Bearer ${token}`,
    };

    this.http.get<any[]>('http://localhost:8090/users', { headers }).subscribe({
      next: (users) => {
        const tableData = users.map((user) => ({
          id: user.id,
          fullName: `${user.firstName} ${user.lastName}`,
          carModel: user.carModel,
          carNumber: user.vehicleNumber,
        }));
        this.dataSource.data = tableData;
      },
      error: (err) => {
        console.error('Failed to fetch users:', err);
      },
    });
  }

  navigateToProfile(driverId: number): void {
    this.router.navigate(['/driver-profile', driverId]);
  }

  setActive(link: string): void {
    this.activeLink = link;
  }

  logout(): void {
    localStorage.removeItem('authToken');
    this.router.navigate(['/login']);
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
    this.dataSource.filter = filterValue;
  }
}
