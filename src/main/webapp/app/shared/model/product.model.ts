import { IImage } from 'app/shared/model/image.model';
import { IProductOrder } from 'app/shared/model/product-order.model';
import { ITag } from 'app/shared/model/tag.model';

export interface IProduct {
    id?: number;
    name?: string;
    price?: number;
    description?: string;
    isActive?: boolean;
    numberOfBatteries?: number;
    images?: IImage[];
    orders?: IProductOrder[];
    tags?: ITag[];
}

export class Product implements IProduct {
    constructor(
        public id?: number,
        public name?: string,
        public price?: number,
        public description?: string,
        public isActive?: boolean,
        public numberOfBatteries?: number,
        public images?: IImage[],
        public orders?: IProductOrder[],
        public tags?: ITag[]
    ) {
        this.isActive = this.isActive || false;
    }
}
