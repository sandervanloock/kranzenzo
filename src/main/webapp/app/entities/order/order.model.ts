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
        public created?: any,
        public updated?: any,
        public state?: OrderState,
        public deliveryType?: DeliveryType, public includeBatteries?: boolean,
        public customerId?: number,
        public deliveryAddressId?: number, public productId?: number, ) {
        this.includeBatteries = false;
    }
}
