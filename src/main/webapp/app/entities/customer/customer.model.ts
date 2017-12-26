import {BaseEntity} from './../../shared';

export class Customer implements BaseEntity {
    constructor(
        public id?: number, public userId?: number,
        public street?: string,
        public city?: string,
        public zipCode?: number, public province?: string, public phoneNumber?: string,
        public addressId?: number,
        public orders?: BaseEntity[], ) {
    }
}
