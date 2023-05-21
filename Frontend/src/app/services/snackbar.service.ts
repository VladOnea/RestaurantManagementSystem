import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class SnackbarService { //for displying the messages

  constructor(private snackBar: MatSnackBar) { }

  openSnackBar(message: string, action: string) {
    if (action === 'error') {
      this.snackBar.open(message, '', {
        horizontalPosition: 'center',
        verticalPosition: 'top',
        duration: 2000, //2 seconds
        panelClass: ['black-snackbar'] //defined in style.scss
      })
    }
    else {
      this.snackBar.open(message, '', {
        horizontalPosition: 'center',
        verticalPosition: 'top',
        duration: 2000, //2 seconds
        panelClass: ['green-snackbar'] //defined in style.scss
      })
    }
  }
}
