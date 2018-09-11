import { IProductOrder } from 'app/shared/model/product-order.model';

export interface ICustomer {
    id?: number;
    street?: string;
    city?: string;
    zipCode?: number;
    province?: string;
    phoneNumber?: string;
    addressId?: number;
    orders?: IProductOrder[];
}

export class Customer implements ICustomer {
    constructor(
        public id?: number,
        public street?: string,
        public city?: string,
        public zipCode?: number,
        public province?: string,
        public phoneNumber?: string,
        public addressId?: number,
        public orders?: IProductOrder[]
    ) {}
}
