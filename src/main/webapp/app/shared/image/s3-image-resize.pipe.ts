import {Pipe, PipeTransform} from '@angular/core';

@Pipe( {name: 's3ImageResize'} )
export class S3ImageResizePipe implements PipeTransform {

    transform( imageUrl: string, size: string ): string {
        const lastIndexOf = imageUrl.lastIndexOf( '/' );
        return ['http://kranzenzo-images.s3-website-eu-west-1.amazonaws.com/', size, imageUrl.slice( lastIndexOf, imageUrl.length )]
            .join( '' )
    }
}
