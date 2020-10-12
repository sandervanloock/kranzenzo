import {Moment} from 'moment';
import {ICustomer} from 'app/shared/model/customer.model';
import {IProduct} from 'app/shared/model/product.model';

export const enum OrderState {
    NEW = 'NEW', PAYED = 'PAYED', CANCELLED = 'CANCELLED', DONE = 'DONE'
}

export const enum DeliveryType {
    DELIVERED = 'DELIVERED',
    PICKUP = 'PICKUP'
}

export const enum PaymentType {
    CASH = 'CASH',
    TRANSFER = 'TRANSFER'
}

export interface IProductOrder {
    id?: number;
    created?: Moment;
    updated?: Moment;
    state?: OrderState;
    deliveryType?: DeliveryType;
    includeBatteries?: boolean;
    description?: string;
    pickupDateTime?: string;
    deliveryPrice?: number;
    totalPrice?: number;
    paymentType?: PaymentType;
    customerId?: number;
    customer?: ICustomer;
    deliveryAddressId?: number;
    productName?: string;
    productId?: number;
    product?: IProduct;
}

export class ProductOrder implements IProductOrder {
    constructor(
        public id?: number,
        public created?: Moment,
        public updated?: Moment,
        public state?: OrderState,
        public deliveryType?: DeliveryType,
        public includeBatteries?: boolean,
        public description?: string,
        public deliveryPrice?: number,
        public totalPrice?: number,
        public paymentType?: PaymentType,
        public customerId?: number,
        public customer?: ICustomer,
        public deliveryAddressId?: number,
        public productName?: string,
        public productId?: number,
        public product?: IProduct
    ) {
        this.includeBatteries = this.includeBatteries || false;
        this.deliveryPrice = this.deliveryPrice || 0;
    }
}
