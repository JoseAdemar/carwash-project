import { Customer } from "../../customer/model/customer.model";

export class Vehicle{
    id: number = 0;
    licensePlate: string = "";
    brand?: string;
    carModel?: string;
    color?: string;
    customer: Customer = new Customer();
}