import { Component, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { WashTypeServiceOrder } from './enums/wash-type-service-order';
import { CommonModule } from '@angular/common';
import { WashTypeStatus } from './enums/wash-Type-status';
import { ServiceOrderService } from './service/service-order.service';
import { ServiceOrderModel } from './model/service-order-model';
import { FormsModule, NgModel } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { VehicleService } from '../vehicle/service/vehicle/vehicle.service';
import { Vehicle } from '../vehicle/model/vehicle.model';

@Component({
  selector: 'app-os',
  standalone: true,
  imports: [CommonModule,FormsModule, HttpClientModule],
  templateUrl: './os.component.html',
  styleUrl: './os.component.css',
})
export class OsComponent {
  washTypeServiceOrderEnum = WashTypeServiceOrder;
  washTypeEnum = WashTypeStatus;
  serviceOrderModel: ServiceOrderModel;
  vehicle: Vehicle;
  @ViewChild('placa') placaRef?: ElementRef;

  constructor(private serviceOrder: ServiceOrderService, private vehicleService: VehicleService) {
    this.serviceOrderModel = new ServiceOrderModel();
    this.vehicle = new Vehicle();
  }

  protected loadVehicleInformationByPlate(): void {
    const plate = this.serviceOrderModel.vehicles[0].licensePlate;
    this.vehicleService
      .getVehicleDataByPlate(plate)
      .subscribe({
        next: (data) => {
          this.vehicle = data;
          if (this.serviceOrderModel) {
            this.serviceOrderModel.vehicles[0] = this.vehicle;
          }
        },
        error: () => {
  
        },
      });
  }

  public createServiceOrder() {
    this.loadVehicleInformationByPlate();
    if (this.serviceOrderModel) {
      this.serviceOrder.createServiceOrder(this.serviceOrderModel).subscribe({
        next: (data) => {
          if (data) {
            this.serviceOrderModel.vehicles[0] = this.vehicle;
            this.serviceOrderModel = data;
          }
          console.log('os cadastrada com sucesso!')
        },
        error: (error) => {},
      });
    } else {
      console.error('serviceOrderModel is undefined');
    }
  }
}
