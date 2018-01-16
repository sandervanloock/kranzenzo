import {BaseEntity} from './../../shared';

export class Image implements BaseEntity {
    constructor( public id?: number, public dataContentType?: string, public data?: any, public productId?: number, ) {
    }
}
