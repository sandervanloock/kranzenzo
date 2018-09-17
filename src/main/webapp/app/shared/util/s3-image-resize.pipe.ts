import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 's3ImageResize'
})
export class S3ImageResizePipe implements PipeTransform {
    transform(imageUrl: string, size: string): string {
        const lastIndexOf = imageUrl.lastIndexOf('/');
        return ['https://images.kranzenzo.be/', size, imageUrl.slice(lastIndexOf, imageUrl.length)].join('');
    }
}
