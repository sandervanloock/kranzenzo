import { IWorkshop } from 'app/shared/model/workshop.model';
import { IProduct } from 'app/shared/model/product.model';
import { IImage } from 'app/shared/model/image.model';

export interface ITag {
    id?: number;
    name?: string;
    homepage?: boolean;
    workshops?: IWorkshop[];
    products?: IProduct[];
    parentId?: number;
    children?: ITag[];
    image?: IImage;
}

export class Tag implements ITag {
    constructor(
        public id?: number,
        public name?: string,
        public homepage?: boolean,
        public workshops?: IWorkshop[],
        public products?: IProduct[],
        public parentId?: number,
        public children?: Tag[],
        public image?: IImage
    ) {
        this.homepage = false;
    }
}
