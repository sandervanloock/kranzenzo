import {BaseEntity} from './../../shared';

export class WorkshopDate implements BaseEntity {
    constructor( public id?: number, public date?: any, public subscriptions?: BaseEntity[], public workshopId?: number, ) {
    }
}
