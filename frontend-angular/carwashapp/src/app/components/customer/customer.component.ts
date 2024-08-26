import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CustomerService } from './service/customer.service';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Customer } from './model/customer/customer.model';

@Component({
  selector: 'app-customer',
  standalone: true,
  imports: [CommonModule, RouterOutlet, FormsModule, HttpClientModule],
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.css',
})
export class CustomerComponent {
  customer: Customer = {
    name: '',
    email: '',
    phoneNumber: '',
    id: 0,
  };

  constructor(private customerService: CustomerService) {}

  onSubmit() {
    this.customerService.addCustomer(this.customer).subscribe(
      (response) => {
        console.log('Customer added successfully', response);
        // Lógica adicional
      },
      (error) => {
        console.error('Error adding customer', error);
        // Lógica para lidar com erros
      }
    );
  }
}
