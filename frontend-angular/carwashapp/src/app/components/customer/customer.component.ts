import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, Renderer2 } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CustomerService } from './service/customer.service';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Customer } from './model/customer.model';
import { NgxPaginationModule } from 'ngx-pagination';
import { Modal } from 'bootstrap';

@Component({
  selector: 'app-customer',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    FormsModule,
    HttpClientModule,
    NgxPaginationModule,
  ],
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.css',
})
export class CustomerComponent implements OnInit {
  protected messageSucess?: string;
  protected isCustomerPersistWithSucess?: boolean;
  protected messageErroInput?: string;
  protected isError?: boolean;
  protected paginator: number = 1;
  private customerId: number = 0;
  protected isLoadCustomerById = false;
  protected customers: Customer[] = [];
  protected param?: any;
  private idInput?: number;
  private nameInput?: string;
  private emailInput?: string;

  public customer: Customer = {
    name: '',
    email: '',
    phoneNumber: '',
    id: 0,
  };

  constructor(
    private customerService: CustomerService,
    private elementRef: ElementRef,
    private render: Renderer2
  ) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  protected onSubmitCustomerForm(): void {
    if (this.isValidatedfield()) {
      this.customerService.addCustomer(this.customer).subscribe({
        next: (data) => {
          this.messageSucess = 'Cliente cadastrado com sucesso';
          this.cleanCustomerForm();
          this.setMessageErroInputEmpty();
          this.isCustomerPersistWithSucess = true;
          this.loadCustomers();
          this.customer = new Customer();
          setTimeout(() => {
            this.isCustomerPersistWithSucess = false;
          }, 4000);
        },
        error: (err) => {
          this.isError = true;
          this.messageSucess = 'Erro ao tentar cadastrar o cliente';
          setTimeout(() => {
            this.isError = false;
            this.cleanCustomerForm();
          }, 4000);
        },
      });
    } else {
      this.showInputMessageObligaton();
    }
  }

  private cleanCustomerForm() {
    this.customer.email = '';
    this.customer.name = '';
    this.customer.phoneNumber = '';
  }

  private showInputMessageObligaton(): string {
    const standerMessageErro = 'Campo obrigatório';
    if (
      this.customer.name === '' ||
      this.customer.email === '' ||
      this.customer.phoneNumber === ''
    ) {
      this.messageErroInput = standerMessageErro;
    } else {
      this.messageErroInput = '';
    }
    return this.messageErroInput;
  }

  private setMessageErroInputEmpty(): void {
    this.messageErroInput = '';
  }

  private isValidatedfield(): boolean {
    if (
      this.customer.name != '' &&
      this.customer.email != '' &&
      this.customer.phoneNumber != ''
    ) {
      return true;
    }
    return false;
  }

  private loadCustomers(): void {
    this.customerService.getCustomers().subscribe({
      next: (data) => {
        this.customers = data;
      },
      error: () => {
        this.isError = true;
      },
    });
  }

  private deleteCustomerById(id: number) {
    return this.customerService.deleteCustomer(id).subscribe(() => {
      this.loadCustomers();
      this.setMessageErroInputEmpty();
    });
  }

  protected openModel(id: number): void {
    const modalElement =
      this.elementRef.nativeElement.querySelector('#myModal');
    const modal = new Modal(modalElement);
    this.customerId = id;
    modal.show();
  }

  protected closeModelDelete(): void {
    const modalElement =
      this.elementRef.nativeElement.querySelector('#myModal');
    const modal = new Modal(modalElement);
    this.deleteCustomerById(this.customerId);
    modal.hide();
  }

  protected closeModelNotDelete(): void {
    const modalElement =
      this.elementRef.nativeElement.querySelector('#myModal');
    const modal = new Modal(modalElement);
    modal.hide();
    this.loadCustomers();
  }

  protected loadCustomerById(id: number): void {
    this.customerService.getCustomerById(id).subscribe({
      next: (data) => {
        this.customerInputInformation(this.customer);
        this.customer.name = data.name;
        this.customer.email = data.email;
        this.customer.phoneNumber = data.phoneNumber;
        this.customer.id = data.id;
        this.isLoadCustomerById = true;
      },
      error: (error) => {},
    });
  }

  private customerInputInformation(customer: Customer) {
    this.customer.name = this.elementRef.nativeElement.querySelector('#name');
    this.customer.email = this.elementRef.nativeElement.querySelector('#email');
    this.customer.phoneNumber =
      this.elementRef.nativeElement.querySelector('#phoneNumber');
    this.customer.id = this.elementRef.nativeElement.querySelector('#id');
    return customer;
  }

  private updateCustomerById(id: number, customer: Customer): void {
    this.customerService.updateCustomer(id, customer).subscribe({
      next: (updatedCustomer) => {
        this.loadCustomers();
        this.isLoadCustomerById = false;
        this.cleanCustomerForm();
        this.setMessageErroInputEmpty();
        this.customer = new Customer();
      },
      error: (error) => {},
    });
  }

  protected submitForm() {
    if (this.isLoadCustomerById) {
      this.updateCustomerById(this.customer.id, this.customer);
    } else {
      this.onSubmitCustomerForm();
    }
  }

  protected cancelarEdicao() {
    this.isLoadCustomerById = false;
    this.cleanCustomerForm();
    this.customer = new Customer();
    this.setMessageErroInputEmpty();
  }

  private searchCustomerByCriteria(
    id?: number,
    name?: string,
    email?: string
  ): void {
    this.customerService.findCustomerByCriteria(id, name, email).subscribe({
      next: (data) => {
        if (data) {
          this.customer = data;
          this.customers = [];
          this.customers.push(this.customer);
          this.validarPreenchimentoInput(id, name, email);
          this.unableRegisterButton();
        } else {
          this.messageSucess = 'Nenhum dado encontrado na pesquisa';
        }
      },
      error: (error) => {
        this.messageSucess = 'Nenhum resultado encontrado na busca';
        this.param = document.querySelector('#searchId');
        this.param.value = this.messageSucess;
      },
    });
  }

  private unableRegisterButton() {
    const button =
      this.elementRef.nativeElement.querySelector('#register-button');
    this.render.setStyle(button, 'display', 'none');
  }

  protected validaInputSearch(): boolean {
    if (
      this.param.value === '' ||
      this.param.value === null ||
      this.param.value === undefined
    ) {
      this.loadCustomers();
      this.cleanCustomerForm();
      this.customer = new Customer();
      this.enableRegisterButton();
      return true;
    }
    return false;
  }

  private enableRegisterButton() {
    const button =
      this.elementRef.nativeElement.querySelector('#register-button');
    this.render.setStyle(button, 'display', '');
  }

  protected searchCustomer() {
    this.validarInputParametros();
    this.searchCustomerByCriteria(
      this.idInput,
      this.nameInput,
      this.emailInput
    );
  }

  private validarInputParametros() {
    this.param = document.querySelector('#searchId');
    if (parseInt(this.param.value)) {
      return (this.idInput = this.param.value);
    } else {
      if (this.param.value.includes('@')) {
        return (this.emailInput = this.param.value);
      } else {
        return (this.nameInput = this.param.value);
      }
    }
  }

  private validarPreenchimentoInput(
    id?: number,
    name?: string,
    email?: string
  ) {
    if (id !== undefined && id !== 0) {
      this.param.value = id;
    } else if (name !== undefined && name !== '') {
      this.param.value = name;
    } else if (email !== undefined && email !== '') {
      this.param.value = email;
    }
  }
}
