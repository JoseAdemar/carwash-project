import { Component, ElementRef, importProvidersFrom, ViewChild } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { CustomerComponent } from "../customer/customer.component";
import { NgClass, NgIf } from '@angular/common';
import { VehicleComponent } from "../vehicle/vehicle.component";
import { OsComponent } from "../os/os.component";
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterModule, CustomerComponent, NgIf, VehicleComponent, NgClass, OsComponent, RouterOutlet, FormsModule, HttpClientModule,],
  providers: [HttpClient],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  activeDiv: string = ''; 

  showDiv(divName: string) {
    this.activeDiv = divName;
  }

  isDivCorpoHomeVisible: boolean = false;

  handleClick(activeDiv:String) {
    if (activeDiv == 'customer'){
      this.showDiv('customer')
    } else if (activeDiv == 'os'){
      this.showDiv('os');
    }else{
      this.showDiv('vehicle')
    }
  }

}
