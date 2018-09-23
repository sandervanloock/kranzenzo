export interface IImage {
    id?: number;
    endpoint?: string;
    dataContentType?: string;
    data?: any;
    workshopName?: string;
    workshopId?: number;
    productName?: string;
    productId?: number;
}

export class Image implements IImage {
    constructor(
        public id?: number,
        public endpoint?: string,
        public dataContentType?: string,
        public data?: any,
        public workshopName?: string,
        public workshopId?: number,
        public productName?: string,
        public productId?: number
    ) {}
}
