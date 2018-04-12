import {BaseEntity} from './../../shared';
import {Image} from '../image';

export class Tag implements BaseEntity {
    constructor( public id?: number, public name?: string, public tags?: BaseEntity[], public image?: Image, ) {
    }
}
