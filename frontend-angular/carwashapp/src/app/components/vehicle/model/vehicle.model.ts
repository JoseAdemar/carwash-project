import { Customer } from "../../customer/model/customer.model";

export class Vehicle{
    id: number;
    licensePlate: string;
    brand: string;
    carModel: string;
    color: string;
    customer: Customer;

    constructor() {
        this.id = 0;
        this.licensePlate = '';
        this.brand = '';
        this.carModel = '';
        this.color = '';
        this.customer = new Customer();
    }
}

