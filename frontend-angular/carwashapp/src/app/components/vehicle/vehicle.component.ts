import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Vehicle } from './model/vehicle.model';
import { VehicleService } from './service/vehicle/vehicle.service';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CustomerService } from '../customer/service/customer.service';
import { Customer } from '../customer/model/customer.model';
import { NgxPaginationModule } from 'ngx-pagination';

@Component({
  selector: 'app-vehicle',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, NgxPaginationModule],
  templateUrl: './vehicle.component.html',
  styleUrl: './vehicle.component.css',
})
export class VehicleComponent implements OnInit {
  vehicles: Vehicle[] = [];
  protected paginator: number = 1;
  protected isVehicleEditing = false;
  protected isSearchingVehicle = false;
  @ViewChild('searchInputLicensePlateValue') licensePlateValue!: ElementRef;

  public vehicle: Vehicle = {
    id: 0,
    licensePlate: '',
    brand: '',
    carModel: '',
    color: '',
    customer: {
      id: 0,
      name: '',
      email: '',
      phoneNumber: '',
    },
  };

  public customer: Customer = {
    id: 0,
    name: '',
    email: '',
    phoneNumber: '',
  };

  ngOnInit(): void {
    this.loadVehicleInformation();
    this.getAllVehicles();
  }

  constructor(
    private vehicleService: VehicleService,
    private customerService: CustomerService
  ) {}

  public loadVehicleInformation(): void {
    if (this.isPlateEmptyValue() && this.isVehicleEditing === false) {
      this.vehicleService.getVehicleData(this.vehicle.licensePlate).subscribe({
        next: (data) => {
          this.vehicle.licensePlate = data.licensePlate;
          this.vehicle.brand = data.brand;
          this.vehicle.carModel = data.carModel;
          this.vehicle.color = data.color;
        },
        error: () => {},
      });
    }
  }

  public loadVehicleInformationByPlate(): void {
      this.vehicleService.getVehicleDataByPlate(this.vehicle.licensePlate).subscribe({
        next: (data) => {
          this.vehicle= data;
          this.vehicles = [];
          this.vehicles.push(data);
        },
        error: () => {
          this.licensePlateValue.nativeElement.value = "Nenhum dado de veículo encontrado na busca"
        },
      });
  }

  public checkIfVehicleLicensePlateIsEmpty(licensePlate: string): void{
    if (licensePlate == null || licensePlate == undefined || licensePlate == ""){
      this.clearVehicleForm();
      this.getAllVehicles();
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
    } else if (
      this.customer.name !== undefined &&
      this.customer.name.includes('@')
    ) {
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
    this.addCustomerToVehicle();
    this.vehicleService.saveVehicleInformation(this.vehicle).subscribe({
      next: (data) => {
        if (data) {
          this.vehicle.licensePlate = data.licensePlate;
          this.vehicle.brand = data.brand;
          this.vehicle.carModel = data.carModel;
          this.vehicle.color = data.color;
          this.vehicle.customer = this.customer;
          this.clearVehicleForm();
          this.getAllVehicles();
        } 
      },
      error: (error) => {
        console.log(error);
        this.clearVehicleForm();
        alert('Não é possível salvar dados de veículo já existente')
      },
    });
  }

  private addCustomerToVehicle() {
    this.vehicle.customer.id = this.customer.id;
    this.vehicle.customer.name = this.customer.name;
    this.vehicle.customer.email = this.customer.email;
    this.vehicle.customer.phoneNumber = this.customer.phoneNumber;
  }

  private clearVehicleForm() {
    this.vehicle.licensePlate = '';
    this.vehicle.brand = '';
    this.vehicle.carModel = '';
    this.vehicle.color = '';
    this.vehicle.customer.id = 0;
    this.vehicle.customer.name = '';
    this.vehicle.customer.email = '';
    this.vehicle.customer.phoneNumber = '';
  }

  public getAllVehicles(){
    this.vehicleService.getAllVehiclesInformation().subscribe({
      next: (data) => {
        if (data) {
          this.vehicles = data;
        }
      }
    })
  }

  public deleteVehicleById(id: number): void{
    this.vehicleService.deleteVehicleById(id).subscribe({
      next: () => {
        this.getAllVehicles();
      }
    });
  }

  public updateDataVehicle(id: number, vehicle: Vehicle) {
    this.vehicleService.updateVehicleInformation(id, vehicle).subscribe({
      next: () => {
        this.getAllVehicles();
        this.clearVehicleForm();
        this.isVehicleEditing = false;
      }
    })
  }

  public getVehicleByIdEdit(id: number){
    this.vehicleService.getVehicleById(id).subscribe({
      next: (data) => {
        this.vehicle.id = data.id;
        this.vehicle.licensePlate = data.licensePlate;
        this.vehicle.brand = data.brand;
        this.vehicle.carModel = data.carModel;
        this.vehicle.color = data.color;
        this.vehicle.customer = data.customer;
        this.isVehicleEditing = true;
      }
    });
  }

  public cancelChange() {
    this.vehicle = new Vehicle();
    this.vehicle.licensePlate = '';
        this.vehicle.brand = '';
        this.vehicle.carModel = '';
        this.vehicle.color = '';
        this.isVehicleEditing = false;
  }
}
