import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ServiceOrderModel } from '../model/service-order-model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ServiceOrderService {
  postServiceOrderApi = "http://localhost:8080/serviceorders/api/create";

  constructor(private http: HttpClient) { }

   public createServiceOrder(serviceOrder: ServiceOrderModel): Observable<ServiceOrderModel> {
       return  this.http.post<ServiceOrderModel>(this.postServiceOrderApi, serviceOrder);
    }
}
