import {BaseEntity} from './../../shared';

export class Customer implements BaseEntity {
    constructor( public id?: number, public addressId?: number, public orders?: BaseEntity[], ) {
    }
}
