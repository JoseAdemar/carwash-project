import { WashTypeServiceOrder } from '../enums/wash-type-service-order';
import { WashTypeStatus } from '../enums/wash-Type-status';
import { Vehicle } from './../../vehicle/model/vehicle.model';
export class ServiceOrderModel {
    id: number
    vehicles: Vehicle[];
    washStatus:  WashTypeStatus
    washType: WashTypeServiceOrder
    price: number
    localDateTime: string

    constructor() {
        this.id = 0;
        this.vehicles = [{  id: 0,
            licensePlate: '',
            brand: '',
            carModel: '',
            color: '',
            customer:{
                 id: 0,
                 name: '',
                 email: '',
                 phoneNumber: '',
            }}];
        this.washStatus = WashTypeStatus.LAVANDO;
        this.washType = WashTypeServiceOrder.BASICA;
        this.price = 0;
        this.localDateTime = '';
    }
}