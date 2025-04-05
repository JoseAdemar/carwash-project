import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ServiceOrderModel } from '../model/service-order-model';
import { catchError, Observable } from 'rxjs';
import { ServiceOrdersDTO } from '../dto/serviceOrdersDto';

@Injectable({
  providedIn: 'root'
})
export class ServiceOrderService {
  postServiceOrderApi = "http://localhost:8080/serviceorders/api/create";
  getAllServiceOrdersAPI = "http://localhost:8080/serviceorders/api/service_order_details";
  patchWashStatusToFinishedAPI = "http://localhost:8080/serviceorders/api";

  constructor(private http: HttpClient) { }

   public createServiceOrder(serviceOrder: ServiceOrderModel): Observable<ServiceOrderModel> {
       return  this.http.post<ServiceOrderModel>(this.postServiceOrderApi, serviceOrder);
    }

    public getAllServiceOrders(serviceOrdersDTO: ServiceOrdersDTO[]): Observable<ServiceOrdersDTO[]> {
      let params = new HttpParams();
      serviceOrdersDTO.forEach(order => {
        // Iterando sobre as propriedades de cada 'order'
        Object.keys(order).forEach(key => {
          const value = order[key as keyof ServiceOrdersDTO];
          if (value !== undefined) {
            // Adiciona a chave e valor ao 'HttpParams'
            params = params.append(key, value.toString());
          }
        });
      });
      return this.http.get<ServiceOrdersDTO[]>(this.getAllServiceOrdersAPI, { params });
    }

    public updateWashStatus(id: number): Observable<any> {
      const url = `${this.patchWashStatusToFinishedAPI}/${id}/wash-status`;
  
      return this.http.patch<any>(url, {})
        .pipe(
          catchError(error => {
            console.error('Erro ao atualizar o status da lavagem', error);
            throw error; 
          })
        );
    }
}
