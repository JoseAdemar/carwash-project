import { Component, ElementRef, ViewChild } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CustomerComponent } from "../customer/customer.component";
import { NgClass, NgIf } from '@angular/common';
import { VehicleComponent } from "../vehicle/vehicle.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterModule, CustomerComponent, NgIf, VehicleComponent, NgClass],
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
    } else{
      this.showDiv('vehicle')
    }
  }

}
