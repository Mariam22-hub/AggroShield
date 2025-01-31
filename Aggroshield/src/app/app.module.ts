import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';

import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';

// ng2-charts Module
import { NgChartsModule } from 'ng2-charts';

// Components
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { DriversComponent } from './drivers/drivers.component';
import { AlertComponent } from './alerts/alerts.component';
import { DriverProfileComponent } from './driver-profile/driver-profile.component';
import { LayoutComponent } from './layout/layout.component';


// Define Routes
const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' }, // Default route
  { path: 'login', component: LoginComponent }, // Home route
  { path: 'signup', component: SignupComponent },
  { path: 'drivers', component: DriversComponent }, 
  { path: 'driver-profile/:id', component: DriverProfileComponent },  
  { path: 'alerts', component: AlertComponent },
  { path: 'dashboard', component: LayoutComponent },
  // { path: '**', redirectTo: '/home' } // Wildcard route for 404s
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    DriversComponent,
    DriverProfileComponent,
    AlertComponent,
    LayoutComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule, // Required for Angular Material animations
    FormsModule, // For forms handling
    HttpClientModule, // For HTTP requests
    RouterModule.forRoot(routes), // Import the routes array

    // Angular Material modules
    MatTableModule, // For Material Data Tables
    MatPaginatorModule, // For table pagination
    MatSortModule, // For sorting tables
    MatIconModule, // For Material icons

    // ng2-charts module
    NgChartsModule // For charting functionality
  ],
  providers: [],
  bootstrap: [AppComponent] // Bootstrapping the root component
})
export class AppModule {}
