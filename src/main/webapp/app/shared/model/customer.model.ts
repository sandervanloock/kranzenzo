import { IProductOrder } from 'app/shared/model/product-order.model';
import { IUser, User } from 'app/core';

export interface ICustomer {
    id?: number;
    user?: IUser;
    street?: string;
    city?: string;
    zipCode?: number;
    province?: string;
    phoneNumber?: string;
    latitude?: number;
    longitude?: number;
    description?: number;
    orders?: IProductOrder[];
}

export class Customer implements ICustomer {
    constructor(
        public id?: number,
        public user?: IUser,
        public street?: string,
        public city?: string,
        public zipCode?: number,
        public province?: string,
        public phoneNumber?: string,
        public latitude?: number,
        public longitude?: number,
        public description?: number,
        public orders?: IProductOrder[]
    ) {
        this.user = user ? user : new User();
    }
}
