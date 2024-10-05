import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Vehicle } from './model/vehicle.model';
import { VehicleService } from './service/vehicle/vehicle.service';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CustomerService } from '../customer/service/customer.service';
import { Customer } from '../customer/model/customer.model';

@Component({
  selector: 'app-vehicle',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './vehicle.component.html',
  styleUrl: './vehicle.component.css',
})
export class VehicleComponent implements OnInit {
  public vehicle: Vehicle = {
    licensePlate: '',
    brand: '',
    carModel: '',
    color: '',
  };
 
  public customer: Customer = {
    name: '',
    email: '',
    phoneNumber: '',
    id: 0,
  };

  ngOnInit(): void {
    this.loadVehicleInformation();
  }

  constructor(
    private vehicleService: VehicleService,
    private customerService: CustomerService
  ) {}

  public loadVehicleInformation(): void {
    if (this.isPlateEmptyValue()) {
      this.vehicleService.getVehicleData(this.vehicle.licensePlate).subscribe({
        next: (data) => {
          this.vehicle.licensePlate = data.licensePlate;
          this.vehicle.brand = data.brand;
          this.vehicle.carModel = data.carModel;
          this.vehicle.color = data.color;
          console.log(data);
        },
        error: () => {},
      });
    }
  }

  private isPlateEmptyValue(): boolean {
    if (
      this.vehicle.licensePlate !== '' ||
      (this.vehicle.licensePlate !== null &&
        this.vehicle.licensePlate.length > 4)
    ) {
      return true;
    }
    return false;
  }

  findCustomerByCriteria() {
    if (!this.isNameFiledEmpty()) {
      this.validarInputParametros();
      this.customerService
        .findCustomerByCriteria(
          this.customer.id,
          this.customer.name,
          this.customer.email
        )
        .subscribe({
          next: (data) => {
            if (data) {
              this.customer = new Customer();
              this.customer.id = data.id;
              this.customer.name = data.name;
              this.customer.email = data.email;
              this.customer.phoneNumber = data.phoneNumber;
            } else {
              console.log('Nenhum dado encontrado na pesquisa');
            }
          },
          error: (error) => {
            this.customer = new Customer();
            console.log('Nenhum resultado encontrado na busca');
          },
        });
    } 
  }

  private validarInputParametros() {
    if (this.customer.name !== undefined && parseInt(this.customer.name)) {
      this.customer.id = parseInt(this.customer.name);
      this.customer.name = '';
      this.customer.email = '';
    } else if (this.customer.name !== undefined && this.customer.name.includes('@')) {
        this.customer.email = this.customer.name;
        this.customer.id = 0;
        this.customer.name = '';
      } else {
          this.customer.name = this.customer.name;
          this.customer.id = 0;
          this.customer.email = '';
      }
    }

  private isNameFiledEmpty(): boolean {
    if (this.customer.name === undefined || this.customer.name === '') {
      return true;
    }
    return false;
  }

  saveVehicleData() {
    console.log(this.vehicle);
    console.log(this.customer);
  }
}
