import {BaseEntity} from './../../shared';

export class Location implements BaseEntity {
    constructor( public id?: number, public latitude?: number, public longitude?: number, public description?: string, ) {
    }
}
