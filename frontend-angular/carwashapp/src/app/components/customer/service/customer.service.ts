import { Customer } from './../model/customer.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private apiUrlPost = 'http://localhost:8080/customers';
  private apiUrlGet = 'http://localhost:8080/customers';
  private apiUrlGetById = 'http://localhost:8080/customers';
  private apiUrlDelete = 'http://localhost:8080/customers';
  private apiUrlUpdate = 'http://localhost:8080/customers';
  private apiUrlGetCriteria = 'http://localhost:8080/customers/search';

  constructor(private http: HttpClient) {}

  addCustomer(customer: Customer): Observable<Customer> {
    return this.http.post<Customer>(this.apiUrlPost, customer);
  }

  getCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(this.apiUrlGet);
  }

  deleteCustomer(id: number): Observable<Customer> {
    return this.http.delete<Customer>(`${this.apiUrlDelete}/${id}`);
  }

  getCustomerById(id: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrlGetById}/${id}`);
  }

  updateCustomer(id: number, customer: Customer): Observable<Customer> {
    return this.http.put<Customer>(`${this.apiUrlUpdate}/${id}`, customer);
  }

  public findCustomerByCriteria(id?: number, name?: string, email?: string): Observable<Customer>{
    let params = new HttpParams();
    if (id){
      params = params.set('id', id.toString());
    }
    if (name) {
      params = params.set('name', name);
    }
    if (email){
      params = params.set('email', email);
    }
    return this.http.get<Customer>(`${this.apiUrlGetCriteria}`, {params});
  }
}
