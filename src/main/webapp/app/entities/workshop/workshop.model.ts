import {BaseEntity} from './../../shared';
import {Image} from '../image';
import {WorkshopDate} from '../workshop-date';

export class Workshop implements BaseEntity {
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
        public showOnHomepage?: boolean, public dates?: WorkshopDate[], public images?: Image[],
        public tags?: BaseEntity[], ) {
        this.isActive = false;
        this.showOnHomepage = false;
    }
}
