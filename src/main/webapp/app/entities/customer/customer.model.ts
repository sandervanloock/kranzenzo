import {BaseEntity, User} from './../../shared';

export class Customer implements BaseEntity {
    constructor( public id?: number,
                 public user?: User,
                 public street?: string,
                 public city?: string,
                 public zipCode?: number,
                 public province?: string,
                 public phoneNumber?: string,
                 public addressId?: number, public latitude?: number, public longitude?: number, public description?: number,
                 public orders?: BaseEntity[], ) {
    }
}
