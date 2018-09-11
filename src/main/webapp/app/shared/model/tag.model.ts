import { IWorkshop } from 'app/shared/model/workshop.model';
import { IProduct } from 'app/shared/model/product.model';

export interface ITag {
    id?: number;
    name?: string;
    workshops?: IWorkshop[];
    products?: IProduct[];
}

export class Tag implements ITag {
    constructor(public id?: number, public name?: string, public workshops?: IWorkshop[], public products?: IProduct[]) {}
}
