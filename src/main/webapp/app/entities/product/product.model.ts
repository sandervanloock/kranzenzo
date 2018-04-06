import {BaseEntity} from './../../shared';
import {Image} from '../image';

export class Product implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public price?: number, public description?: string, public isActive?: boolean, public numberOfBatteries?: number,
        public images?: Image[],
        public orders?: BaseEntity[],
        public tags?: BaseEntity[], ) {
        this.isActive = true;
    }
}
