import { IWorkshopDate } from 'app/shared/model/workshop-date.model';
import { IImage } from 'app/shared/model/image.model';
import { ITag } from 'app/shared/model/tag.model';

export interface IWorkshop {
    id?: number;
    name?: string;
    description?: string;
    neededMaterials?: string;
    includedInPrice?: string;
    durationInMinutes?: number;
    price?: number;
    maxSubscriptions?: number;
    isActive?: boolean;
    showOnHomepage?: boolean;
    dates?: IWorkshopDate[];
    images?: IImage[];
    tags?: ITag[];
}

export class Workshop implements IWorkshop {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public neededMaterials?: string,
        public includedInPrice?: string,
        public durationInMinutes?: number,
        public price?: number,
        public maxSubscriptions?: number,
        public isActive?: boolean,
        public showOnHomepage?: boolean,
        public dates?: IWorkshopDate[],
        public images?: IImage[],
        public tags?: ITag[]
    ) {
        this.isActive = this.isActive || false;
        this.showOnHomepage = this.showOnHomepage || false;
    }
}
