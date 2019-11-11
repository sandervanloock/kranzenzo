import { IImage } from 'app/shared/model/image.model';

export interface IHomepageSetting {
    id?: number;
    title?: string;
    description?: string;
    image?: IImage;
}

export class HomepageSetting implements IHomepageSetting {
    constructor(public id?: number, public title?: string, public description?: string, public image?: IImage) {}
}
