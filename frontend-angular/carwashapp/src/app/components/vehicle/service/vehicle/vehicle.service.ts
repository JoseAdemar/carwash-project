import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Vehicle } from '../../model/vehicle.model';
import { Customer } from '../../../customer/model/customer.model';


@Injectable({
  providedIn: 'root',
})
export class VehicleService {
  private getApiVehicle = 'http://localhost:8080/vehicles/api/api-plate';

  constructor(private http: HttpClient) {}

  public getVehicleData(licensePlate: string): Observable<Vehicle> {
    let params = new HttpParams();
    params = params.set('licensePlate', licensePlate.toString());
    return this.http.get<Vehicle>(`${this.getApiVehicle}`, {params});
  }
}
