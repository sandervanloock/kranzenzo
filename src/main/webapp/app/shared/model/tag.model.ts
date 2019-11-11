import { IWorkshop } from 'app/shared/model/workshop.model';
import { IProduct } from 'app/shared/model/product.model';

export interface ITag {
  id?: number;
  name?: string;
  homepage?: boolean;
  workshops?: IWorkshop[];
  products?: IProduct[];
  imageId?: number;
}

export class Tag implements ITag {
  constructor(
    public id?: number,
    public name?: string,
    public homepage?: boolean,
    public workshops?: IWorkshop[],
    public products?: IProduct[],
    public imageId?: number
  ) {
    this.homepage = this.homepage || false;
  }
}
