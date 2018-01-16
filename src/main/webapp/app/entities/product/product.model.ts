import {BaseEntity} from './../../shared';

export class Product implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public price?: number,
        public description?: string,
        public isActive?: boolean,
        public images?: BaseEntity[],
        public orders?: BaseEntity[],
        public tags?: BaseEntity[], ) {
        this.isActive = false;
    }
}
