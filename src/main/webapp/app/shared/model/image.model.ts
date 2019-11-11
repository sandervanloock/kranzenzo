export interface IImage {
  id?: number;
  dataContentType?: string;
  data?: any;
  endpoint?: string;
  workshopName?: string;
  workshopId?: number;
  productName?: string;
  productId?: number;
}

export class Image implements IImage {
  constructor(
    public id?: number,
    public dataContentType?: string,
    public data?: any,
    public endpoint?: string,
    public workshopName?: string,
    public workshopId?: number,
    public productName?: string,
    public productId?: number
  ) {}
}
