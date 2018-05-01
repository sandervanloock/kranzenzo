import {BaseEntity} from './../../shared';
import {Image} from '../image';

export class Tag implements BaseEntity {
    constructor( public id?: number,
                 public name?: string,
                 public homepage?: boolean,
                 public tags?: BaseEntity[],
                 public image?: Image,
                 public parentId?: number,
                 public children?: Tag[] ) {
        this.homepage = false;
    }
}
