import {BaseEntity} from './../../shared';

const enum OrderState {
    'NEW', 'PAYED', 'CANCELLED', 'DONE'
}

const enum DeliveryType {
    'DELIVERED', 'PICKUP'
}

export class Order implements BaseEntity {
    constructor(
        public id?: number,
        public created?: any, public updated?: any, public state?: OrderState, public deliveryType: DeliveryType = DeliveryType.PICKUP,
        public includeBatteries?: boolean, public description?: string, public deliveryPrice?: number,
        public customerId?: number, public deliveryAddressId?: number, public productId?: number, ) {
        this.includeBatteries = false;
    }
}
