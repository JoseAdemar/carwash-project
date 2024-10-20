import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Vehicle } from '../../model/vehicle.model';

@Injectable({
  providedIn: 'root',
})
export class VehicleService {
  private getVehicleApi = 'http://localhost:8080/vehicles/api/api-plate';
  private postVehicleApi = 'http://localhost:8080/vehicles/api';
  private getAllVehicleApi = 'http://localhost:8080/vehicles/api';
  private deleteVehicleApi = 'http://localhost:8080/vehicles/api';
  private putVehicleApi = 'http://localhost:8080/vehicles/api';
  private getVehicleByIdApi = 'http://localhost:8080/vehicles/api';
  private getVehicleApiByPlate = 'http://localhost:8080/vehicles/api/plate';

  constructor(private http: HttpClient) {}

  public getVehicleData(licensePlate: string): Observable<Vehicle> {
    let params = new HttpParams();
    params = params.set('licensePlate', licensePlate.toString());
    return this.http.get<Vehicle>(`${this.getVehicleApi}`, { params });
  }

  public getVehicleDataByPlate(licensePlate: string): Observable<Vehicle> {
    let params = new HttpParams();
    params = params.set('plate', licensePlate.toString());
    return this.http.get<Vehicle>(`${this.getVehicleApiByPlate}`, { params });
  }

  public saveVehicleInformation(vehicle: Vehicle): Observable<Vehicle> {
     var teste = this.http.post<Vehicle>(this.postVehicleApi, vehicle);
     return teste;
  }

  public getAllVehiclesInformation(): Observable<Vehicle[]>{
    return this.http.get<Vehicle[]>(this.getAllVehicleApi);
  }

  deleteVehicleById(id: number): Observable<Vehicle> {
    return this.http.delete<Vehicle>(`${this.deleteVehicleApi}/${id}`);
  }

  public updateVehicleInformation(id: number, vehicle: Vehicle): Observable<Vehicle>{
    return this.http.put<Vehicle>(`${this.putVehicleApi}/${id}`, vehicle);
  }

  getVehicleById(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.getVehicleByIdApi}/${id}`);
  }

}
