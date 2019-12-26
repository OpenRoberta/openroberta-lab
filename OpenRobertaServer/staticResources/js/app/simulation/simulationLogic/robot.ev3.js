define([ 'simulation.simulation', 'interpreter.constants', 'simulation.robot', 'guiState.controller' ], function(SIM, C, Robot, GUISTATE_C) {

    /**
     * Creates a new Ev3 for a simulation.
     * 
     * This Ev3 is a differential drive Ev3. It has two wheels directly
     * connected to motors and several sensors. Each component of the Ev3 has a
     * position in the Ev3s coordinate system. The Ev3 itself has a pose in the
     * global coordinate system (x, y, theta).
     * 
     * @class
     */
    function Ev3(pose, num, robotBehaviour) {
        Robot.call(this, pose, robotBehaviour);
        that = this;

        num = num || 0;
        this.id = num;
        this.left = 0;
        this.right = 0;
        this.ultraSensor = {
            x : 0,
            y : -20,
            rx : 0,
            ry : 0,
            distance : 0,
            u : [],
            cx : 0,
            cy : 0,
            color : '#FF69B4'
        };
        this.geom = {
            x : -20,
            y : -20,
            w : 40,
            h : 50,
            color : '#FCCC00'
        };
        this.wheelLeft = {
            x : 16,
            y : -8,
            w : 8,
            h : 16,
            color : '#000000'
        };
        this.wheelRight = {
            x : -24,
            y : -8,
            w : 8,
            h : 16,
            color : '#000000'
        };
        this.wheelBack = {
            x : -2.5,
            y : 30,
            w : 5,
            h : 5,
            color : '#000000'
        };
        this.led = {
            x : 0,
            y : 10,
            color : 'LIGHTGREY',
            blinkColor : 'LIGHTGREY',
            mode : '',
            timer : 0
        };
        this.encoder = {
            left : 0,
            right : 0
        };
        this.colorSensor = {
            x : 0,
            y : -15,
            rx : 0,
            ry : 0,
            r : 5,
            colorValue : 0,
            lightValue : 0,
            color : 'grey'
        };
        this.touchSensor = {
            x : 0,
            y : -25,
            x1 : 0,
            y1 : 0,
            x2 : 0,
            y2 : 0,
            value : 0,
            color : '#FFCC33'
        };
        this.gyroSensor = {
            value : 0,
            color : '#000'
        };
        this.display = {
            OLDGLASSES : '<image  width="178" height="128" alt="old glasses" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAQAAAAFMftFAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAAFeAAABXgAY1ULDgAAAMxSURBVHja7Z3blqowEESJy///Zc7TjA6XpDvpSzWn9tOohGyKNiBoZtsIIYQQQgghhBBCCCGEEEIIIYQQQggBpZmtac/eFFPscjFY2d9oTdXSMN+m+VVcV279mB22a67xs4YGKdNB6xv+nwH/MBW0rpFtwJFDi6W52lvTwL6Go2K2Nld6Sxf3GyS8g5YcyGa2TuEtW9R7HPYL+sq8iZc0spYsuItar+0Ij5h1J2OO1TxebFe0XAnaOmZ5Da/Yi6xfE6rb1hwqz3ZI0kfsaK3ft/bVYB/EXMSz7sM19yo5qhqsifYe7pyXqql/xOuDxqz3Ss+Dti9Fs5gqXos5y7trPTrw6VRtDl7za5mvYtdPAm+hbIWxeAa7cPf7jF6Xix8BqIZJfM+FhGuTDBeSk58gXWWryIg7nEPWdu1Twfp16r3DrMeVDFINE/TMQ72PIes691T1NPHzvlzzqJJhqkHNvXm49+jiNZDq0pZFep96fusWD1TVnJuDn9fLP/H1N8oDjLMWA3r3NvLecLbbFW9+6H2uktGBGixGY/IVoXUs6UycaNo7sFgle9z38kdbycHV0H47bF/PfYtUCP1TydCHsyZ4ZkDk9h36encXbf3GMbTzn/vPgxuhHavA4cfkdvVnu3i1T+r79GM5+hJL9nCif1dlmrfrB9khjpX37jNY/AlZf56cw37xGGrc7VGjkutRspKP4qVKokrI7eJxmaDhT+G2bbsbfcuMyRVCbhOvQFEh5B4lYv6EjKqL6qVwrl7JJWDIAaCHXHGwOIEe8pgCu+E75AK6JTjliF3JD9nt2CE/BIgbTEK3e8CtWckBnHNHqYrM6U6MrVnJAVj/1t/Tqw+wNSs5gNyvmOqsRsBaI34H2XpCwHRrDhcB4P3wZu2jNKQ12k/I1q9WAFpj/d7Ub3KcVOvRApHCNedyFlj7TX/kIKsAyjpuPhYDWRVA1k+eRhLGOn9C1OiZOhOsc6f2rTEJ2rJ17lXbCpOgGVjnXVKMvUmaap1zvSvnLnSadfwVr9zb/CnWtv/MxbcvO4Kt/T/K4kT7HGtCCCGEEEIIIYQQQgghhBBCCCGEEEIIId/8A/WUnnVgvvqBAAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE2LTAzLTEzVDE0OjE3OjMyKzAxOjAw2vjCoQAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNi0wMy0xM1QxNDoxNzozMiswMTowMKuleh0AAAAASUVORK5CYII=" />',
            EYESOPEN : '<image width="178" height="128" alt="eyes open" xlink:href="data:image/png;base64,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" />',
            EYESCLOSED : '<image width="178" height="128" alt="eyes closed" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAQAAAAFMftFAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAAFeAAABXgAY1ULDgAAATbSURBVHja7ZxJksMgDEUhlftfmV7EiW3MIEDDh+ZvutoDoBcZgwR2bmtra2tra2tra2srlrdugJKCpbX/A3Kwtfdlbb+CwngRY1ofsjni9buLJ2IDi9f2ZAAvdm5tyCCIV+4uUoj3EI5VEH0xQNWCihEbW7ki5DtiAAsBmsCsK2IQ60CawSZAxEANYREk4nXHyUCIwRozKONYW14ozRkHBIsYo0E8o9owdLeo3qa180UXgBFbQuYM30AjtoIsEx8DRWzRsBrg9haZJkkp0vXkEuAxSMCIdRuXRgyNh0d6JoKFHzWlY+o/BqxlLlx8V1vyJv97xPJGgwYfdSVrOHDQRlOSxoNPdvUkBwB+HqYn2RmfHWKgpS3reRol9KRu80qQW2J7qnavApkypzSbdyJAHu8/W0bjBiN3S8g8/WcPNOo9TLNVG8itmZF8K/unO+k7RSLeCJkRSv9Zu6rHDr6fWurGTrU+3KXrOWaUKiMSq8xIS73p+7gm7XTME0DmerT97QhH+6mYu+vSWnA4Go+LPdhi0013nbrpJ+5Hm6v1wr6s4cl8IU9f+I+vXHbJQ+aNKiPMUJslDZk/cO+jv7ylikjnxcdpAsw+U7pkg/ZyQPg9z8st5JWEvF6OLzSEk75HvMaCw3UQ55SPswRpBDJ+LJuerXcYvnBlLoTlpT0Z0495JzXlQKxKd4GlkDnanomhpRO8c3KQRx5r/nVH9W6gFXNTnWieHB7/aUWLJexQGML1NSx1tD+tSlWthjhFVb4+eo75IXN5T+k9zlu37zhTb8XlXj7Iox+kyWWGvQvNk4Ca/MDZs06KNzvnOCDbxhL4N6zV5X+JA2LpvZC50dbKC9mhfgxAQ/6oO+3PjyN9kO281x/GSYTv24adJ+jqHUiji7vBlBef9XzSF/z5IlTI1n09VSR/nvXzOPw/QX+J/rg/W0IfZImQOfW8bBfRu2ChCHpWT+b25fHSCu8QHMiefE7Ol0dDnhl/loQs/eriz8mNK+nP49+uolQ5Uq4vXse5Fo7vCYlK7PVkqUfW30r22Xr0X709ZQ3n+Kw3c3Gvr+Nv669kWcj4mKWXLQTnkEYXI4b0vbiCOOKjZPnkjlb6nmdjjohwYxetaklTKe/l00hTSpiRr3uazZK928B6DW8VfaVP6S7Tbb/0lE6oXCH1YuGLNKgoD7m0TzMdDCn9YEgrlEE+xRAq51q2eXFjpk2CpvioSB5N/2ITvT131mmpSLnJyBm2G1fPMpW0pkRcnvHxb6bpnZuN3musEuSQOdYDvz4iKWtawM6VIHOvab9ibkM2sQ/Hpt/NSp8dXco69imGEYtM9YxdyEamroOrUk3heQCQHtnolGnto+c29XwzxTJSMqjn8Ko8ctaeubW+MAERf7oLSmRVYmtX63ataeUbpsbonzezbmNW3xdf/YNKkM2fQ6+Ei4TEf3MgBm3luxKvnAkxrG7jZH/743AQTz7ju0yrYRFTBdvSH+TpEQPr6C4ixFifA5m8szggJxFjAKYLuL2vRRBD67URy+vz4kNGPH2P/IGMjJgq6Fa/o6w0dGNn1ae72IhFdeYcMAFPHKo/9YJGvIjuo4stEWHjnTofcmqFjTngiNeADK8NWUHIj9oSwzfntieraENWEC7kBaJvX+FCXkgbsoL+APENFhEdy5OyAAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE2LTAzLTEzVDE0OjI0OjIzKzAxOjAwwvAUiQAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNi0wMy0xM1QxNDoyNDoyMyswMTowMLOtrDUAAAAASUVORK5CYII=" />',
            FLOWERS : '<image width="178" height="128" alt="flowers" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAQAAAAFMftFAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAAFeAAABXgAY1ULDgAAAXJSURBVHja7V3bkuwgCIxT8/+/7HnZmclFpBFQ9NgPW7WJUewQVEDnODY2NjY2Nv4jpG4t5YFtD0aPjubq3YhUXyVWS+jdxQyVikM0La9CRs/uYQT3kEQqcQKuhuiajGJfWSTypoa7gzomp9hTGlTepC5TxMtR5H7P2YCjL5S50FE1Qpsz3C5e8gR7TdZqY39tlrYoltDHXMwHVDubvjNrki30cKxldsDWZKmVTYdYDWxJttLBxXR5a3IHvEcL0AXGDh8pLEm2/MizERWl5XDzyq0Va2tyeVBLf/e60bwuyZy+prbVWwvQRpC4hu2cQNd9zCTkr0LjrTW8Gl6Tc/V6BC8wBV621GeyiDj30DriaLJE22Se4ibVojUZp6ybbXOR56fN+XGHeqJUvtIiRbLcMxWJ5hZk4sp98kctqys6Xia55cOfdSnMyf38LmqRwKKyJaLa0Wj/LmTGAu/rdZAURQK9J2Kt+MklTYppGfRaZaPrvJR5F4vEgPfk0SfYW5gWhvTC5ePIgK3UKYRfPP0xNF5JDqLHsJK2E+3f01MLITW5tTNhcNOSM8kRxUWQgSuy+8ZPJm0Fohb82iktGWhoJJDMXP7Kero668tSW6pDrzh/5sLavcNPdWxpyZX/DvietB0IHgMfTp810Z9aG8nwgj3JUtrsaP7RGoxma5JbKIuUI+0CW5Jb6bKi+a7LQWi2JFlDlb02p6/R0C7AW3CZRtpN4bQ0ecTb6KhHV8y/rL7iGULymcGIpLAi2aIjsfavaKU8lXufLoYYJMzxHAy9jcdjWW+jyVY6aK3L+RQQsp/BwPfXTdOqZVTovlraAUW0eCZ5TYPhY+nRGHbhUoQtjtoXfV2G0LK1tMNvZSi29+KLTAmeQnlf09/fRNwl7f7KNpkjUmYe0+NJ+DW9KlXNCJn8koy55//wK3pO4Wan+TgkmRlYb6lSIM2lefIKNOPgZtC0DYbxJiqYczKncbU6npFEDXyz0qyFbG9qxgK4r0oVs5mNsPLWfRcY0bb79xYEP08OFpRk5AwJ1AsX3Xikwn8SxXBNjkFJzo7pIvp6YisASHJsY6F3Ajn3DyEZEWHciS3qpcK3vPS4ENh/wZMcWYtrIwVul92TFTmSZVsmNZA+jw7F0nCRg4mxzePVbQyzbyUzz9DbIbG6QUmsk6XbaOba0e6zLtVCeeow8kT+5MRUJIecEKQdG5qRWh1O7bQnWUoI2op2cMLPITI/f9Y20CilxMcSW8D0JGUvknlR7O09v/dZAsMzwT1JplrwMENZWB6FyWFnyuhVZ9StpKR/XTF/SkBm7ta/pC4vYX6Sr+AyN0vrO3ei5zIXT3nrWnmdzw4zK6toMu2Lu29oKOt6CKd9TGDf23lhfU226jQszk3yB4m9lwlKE2RMlFiD5OBYgWTkVCBe1x11mSI5xCR+FaygyTzcfgsHw9wkT/K91XLh4hExCal3zK3Jk9BeIznyVt4zrM7OcpO1hyb7Gh6LvNOhGUT64xV88TnLwiJPz1FWLPitr9s+mMXlVNzLcTEgR5KR/OQI+1SvkBI8VlqHqDJVr130G4sZY1G/oU57RGRprTaGBzcAqGzu5kJStf6Hvi0MT7se12R0JVparfYn6/WGR0vygAHQKkHQ46j+Ur14Dg/VDm9kzGlui/FpT4DTGx5N6/Q9p0XJGN9F/zNpJTCnely0mtpu4Eluwy8rWGB0SkBML5pxgsBokr1ouM8U9jGSAkjOWhmSY1HCbCT/IJ+xD6N5PpL7OJ5MMR/JP5T3ewdMlYw5unOg8t91C2PUgSrGnCSb7ugo1LlJ/qJMNOLV6Jx9P+s8+TieXhDcK3J11btb8Zk1+Ql8yy4NB0Zm1uR2UDofxGkfG+i+6FKy7NBo9crY2xma0eNIKhFWJDkc1rLJ9QWFWwyPw7o2ufNcuIbVNNn3qJ1GbJvcAetpcsCjGVYk+Ti6rud4rErycQw412JjY2NDiX8qWUw1YXhEAQAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAxNi0wMy0xM1QxNDoyNTowMiswMTowMMlgc34AAAAldEVYdGRhdGU6bW9kaWZ5ADIwMTYtMDMtMTNUMTQ6MjU6MDIrMDE6MDC4PcvCAAAAAElFTkSuQmCC" />',
            STEERING : '<image width="178" height="128" alt="steering wheel" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAYAAACvODPOAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAUWaVRYdFhNTDpjb20uYWRvYmUueG1wAAAAAAA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjYtYzE0OCA3OS4xNjQwMzYsIDIwMTkvMDgvMTMtMDE6MDY6NTcgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1sbnM6ZGM9Imh0dHA6Ly9wdXJsLm9yZy9kYy9lbGVtZW50cy8xLjEvIiB4bWxuczpwaG90b3Nob3A9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGhvdG9zaG9wLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdEV2dD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlRXZlbnQjIiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCAyMS4wIChXaW5kb3dzKSIgeG1wOkNyZWF0ZURhdGU9IjIwMTktMTItMjBUMDk6NDk6MTMtMDU6MDAiIHhtcDpNb2RpZnlEYXRlPSIyMDE5LTEyLTI2VDE2OjMyOjM5LTA1OjAwIiB4bXA6TWV0YWRhdGFEYXRlPSIyMDE5LTEyLTI2VDE2OjMyOjM5LTA1OjAwIiBkYzpmb3JtYXQ9ImltYWdlL3BuZyIgcGhvdG9zaG9wOkNvbG9yTW9kZT0iMyIgcGhvdG9zaG9wOklDQ1Byb2ZpbGU9InNSR0IgSUVDNjE5NjYtMi4xIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjcxYTU3MzYyLTY0ZmQtNWM0MS05MTcyLWIxNmE3N2I2YmYzMiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo3MWE1NzM2Mi02NGZkLTVjNDEtOTE3Mi1iMTZhNzdiNmJmMzIiIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo3MWE1NzM2Mi02NGZkLTVjNDEtOTE3Mi1iMTZhNzdiNmJmMzIiPiA8eG1wTU06SGlzdG9yeT4gPHJkZjpTZXE+IDxyZGY6bGkgc3RFdnQ6YWN0aW9uPSJjcmVhdGVkIiBzdEV2dDppbnN0YW5jZUlEPSJ4bXAuaWlkOjcxYTU3MzYyLTY0ZmQtNWM0MS05MTcyLWIxNmE3N2I2YmYzMiIgc3RFdnQ6d2hlbj0iMjAxOS0xMi0yMFQwOTo0OToxMy0wNTowMCIgc3RFdnQ6c29mdHdhcmVBZ2VudD0iQWRvYmUgUGhvdG9zaG9wIDIxLjAgKFdpbmRvd3MpIi8+IDwvcmRmOlNlcT4gPC94bXBNTTpIaXN0b3J5PiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgQrxOoAADV1SURBVHhe7V0HfFTF9j676Q1CJwFD6B2UrjQBQUUURPShgIgoSvWpgEpTFOyCIIigSNWHgoBI772j9CK9Se+Qnsz/fGfu7N4EfM/3/glml/3ym9w2t+1898yZM2fOOBSDfPDBw+G0lj5kIgYPHkwxMTHUv39/a48PWQ2fRM5kXL9+ncqVK0cnT52iQlFRtGzZMipevLh11Iesgk8iZzIWLFhIUQUKUFpKCh0/fpzmzJlrHfEhK+EjciZj7LdjKSw8wtoiWrRoobXmQ1bCR+RMRqOGD1BcfLy15aBt27ZZ6z5kJXxEzmQkJIDEutnhcPp+3tsF3y+dydi1Zyfly5tX1lVaGrPZIes+ZC18RM5klC9bnvz8/a0tRZUrVrTWfchK+IicyXjuuQ504cIFa4uodp061poPWQkfkTMZ+fPnIz/Wjf2cWirXqFFDlj5kLXxEzmT4+fnRtbh4Cg4Jku3o6GhZ+pC18BE5k+Hgxl3pkiUoMTFRtgMCAmTpQ9bCR+RMBoi8evVqgsEC8HkA3B74iJzJOHPmjCz9/P34v4MWLVok2z5kLXxOQ5mMSpUq0Y4dO8g/IIhS01KpcHQUHTlyhJy+zpEshe/XzWRMnTqVatasyaoFk7hwIfrqq698JL4N8P3CmYzSpUtTvvz5KC01hQKYwE2bNrWO+JCV8Aginz17Vvx8PUELiouLo7x5dBd1amoq7d69O9s/d0pyCm3fvt1qpFqtVA9Dtify4sWLqVq1alSgQAHq2bOXkDo7Y9y48XTs6FFZP3biJFWpUpW2bs2eHnApls/0sOHDqHLlylS3bl0aOnSYddSzkO2JvG/fPjp/4YJIuiFDPhNSL1261Dqa/XD16lUKDAqUdX//AEpMTKAgazs74caNG/Tbb79RmzZtqP+AfhQcGi77c+bMIUtPQ7Yn8uOPt6QPP/xQ1gMCQ0SCNGrUiN5++23Zl52Aahlmt6vXrll7tOfb2bPnZJldkJycLCQeOHAgrVq1iuLjEigpMZHuuusu6tjxeSuXhwHmt+yOKVOmKK72oGgqp3+Q8vMPVPyjq0mTJlk5/n4sW7ZMde3aVUVG5lY5cuSSZ+QPj5cBqmHDhqpfv/5Wzr8fa9asUY+3bCm/JyywgcFh8nuOHj3ayuF58AgiJyQkqG/HjVMPPvig9eM7VUBAkPz4y5Ytt3L9feBGnWrcuLH1bLdOeNbTp09bZ/x92LFjh5oxY6b1XE4VxCTGeu/evawcngmP6RCBa+SkyZOlKpz+00+yz+kXQIWiC9LevXspNDRU9mU18HOhw+P333/n5U7KkycPFS0aS1On/UQ5IsIpR44cFB4eJs+TnJxC8fHxdPnyZUlPtGpFcayb7tq1i/LlyyemOjSysH47AMvPkiVLqGuXLnTy1Bn+/fwoLSWJXur0Io36arR0r3sqPKpn7zqTYDUTeTITetasn+nateuyv0WL5jR16jRuXBmH9swFGppz5syhAwcOiH557tw5KlGihFhS/PxwTyXOQTC3mS7qU6dPUWTOXBTB5AapQ0JCKSU1hYICA+Ualy5doj/+OE0l+ToHDx2gsLBwevDBJlShQgU5P7ORlJRE33//PQ185x06YllVoBMj9kbHjh09vtPG47qoYTKCdJ4xYzp98MGHTJzzlJgYR0899ZT0ouXKlcvK+f8HyDtv3jwxSx04cJCJm48lbCJ/MH7c6r9OBw8cogsXL8jzXOTllctX6PKVK0IaWAWAyMhIJnIIS+kIyps3j0jw3LlyU1R0NMXEFJH2YFBgACUkJFOZMqWkERYREcGkfpCKFOHjmQA8S9u2bWnNmrX8EWrzZbFiRWnu3HlSK3gDPNbXAhaCjz76iPr06UPcAOQqMpFq1apJnTt3pYYNG1DhwoWtnP8dcN2hQ4fShg0b6B2WXiDpmTOnaeu27XT61B+0b+8+lmhHeP00S9hU66yb8XjLJ2jThvV04uRJa8/NiMwVSVFRUVSWyVQgKprVpCh+9kYirffs2UfLli0Ri83/KqVhCpw+fQZL4u+085LDjyuPVGJ9nmu0WRQcHGzl9Hx4LJEBPHr//gNo4sQJdObcRUpK0FIQTjr/izQDcUHQMmXL8kdRi7aydJw3fx4dZeKihw46rx1+/oHpquQ0fp7UZNY5X+oktcP+A/upYvmKlJSSmk7twXOnpCTzSvpeNEjsKlWqUMGCUSxB27HOSnT48GH6+eef5eMqWbKklfOv4dq1a6KzA/6BIRRVIC81bfowjRr1lUfrw7cEiOzpePrpp5XDyS3wkHB8lKpb9x7Wkb8GmPcaN26iPv7kY7Vu3To1ZMgQ1aBBA5UzZ065nkncuFT+gcEq4FYpKESWkA1VqlRVx4+fUJ9/Pkwx0bUpTo7rPBmTn38QP79funtVqFBevdKjh5o6dapaunSp4tpH1bv/frGQ/FV07vyyPI+xTMBy4q3wCiKDfCgkFBZIgXWQ+6233lIDBw60ct0M1mVVg4YN1dy5c9Whw4cVS1F1zz33qODgIBeh/AOCMpAO9mFeBzE5gdgwY5n8JnG1fdM+JKdlXzbnu65nS04/f1f+IjExqnmLFop1Z7V+/Xp5t++++856g5uxZ88exTWSev755yUv7O7mWi+//LKVy/vgFUQGuEGjuHEmhQcbsyk8bA8c+K5ivVP985//dEm0FStWyLGffvpJrVi+XNWpU0eFhoRY5zmEUHYSyzoTLygkTMhnrm9SeHiYqlSpkmrS5EH18MNNxa7ctGlT9SBv161bT0VHR990DlJgUJgmtXUfc6+g4FDFOq0rX7FixdSbb76pTp48yc88TZ7dAB8t7MNnz55VW7dulbw4BzUI8v3666+KdX8rt3fCo3XkWwEhqu6++25xbMebKdZD01KTKSDAX3Tc8+fP0xtvvEn5CxSg9wcP4gbjh/TJJ59wo+6inI/zDESNdDjxsVNKUhJvuHXaShUrUPvnnqOYIrHk5EZUXNx1On/xIh07epzOnj1Dl7nBFhISRAUKRlN0dCHKnSsn68D5KTAoiG7cuEorV6wUPdoOlu5ad+X7odFp9O80blSm8jsA9erVpWHDvmB9uQSVZV1+0KDB1K9fX+m6b9asGe3es4sOHTws+ntqShKt4PvgHK8HiOxNGDr0c9WtR3dLIulqG5JJpJsl4d584w0VnxDvklxIporH0qxzA8l1HKly5cpq8uTJasHCxeqDDz5UsbFF0x03iRt2yuFg3Zj1Y6zfKs+TTz6pfv55llq1eq3q3KWLypU7j+uY1sO12uF6FpbS8h58PJJ1948//lglJiaqQpakd1jHiHVt/4BAlvT62e2S25vhdUSG6jBgwNtq85YtUpAggWnsIK1avUrt2LlLRUZGWvuckkfIAtJY1bqfv1vH/eyzT9Xk775Xffv0UTly6Aagn5+fatSokVT3M2fOVKtWrVL16t3vOsfhBIEdsh4VFaVGjxkjVfzw4cNV27bPqtKlS7vyPtKsmRo2bLj65ptvVe37arv2B/Bz4FlAYpOc/hZhOXXp1k3euVq1arLtxx8u8mgSO4XEf/zxh+TxdngdkQ1AGBSuvyWZkE6ePK5mz/5FSIhtQ2AXkVkK2vMPfv999Ss3sgoXLuza16lTJ7Vu7Vp1+fJl607pwVW55DMfD5xzboWUlBR18OBBJu83qkDB/K7rDx/+hVq7dp2Ktt0TEtpIZxAVz+pwakLnzV9ArvfAAw/IdmAQiK8/wjsJXvm2IBmkkSaClornzp1Ts2bNsvaBxExaixQgiEgxVgdwrHXrp9WGjZu5wfaQbMMMN3XaNOvqNwOkTE5JlvXNmzfJOaZqb9uunez/Tzh37oxq06adnIM0jRuh48aNU06Hfv7gkHAVGOxWN4K4kWjysl4t13jyqadk26hE8HK7U+CVREYVDiJ//PEnUqCnTv6RjsQtLRdGmOpACpDEHPv9931q8OAPXNtTf/zRuuqfw27b3bt3r5wXGKSl4rPt21tH0uf7M/z++37VvPnjci7s0auZjOZ5HY4A+UDw8WG7RPESqnzFCrKeN29eOT+qUJRs4/3nzZsn++4EeHX9g8JctHipmjt/gRQukkGft/rKtpFeZcuWUVeuXHHl69mzp5VTA+YrSF47QEw7Oa9du6YWL16krxugr4tG3eXLl6wcWnpnPO9WiI+PF0mLa/zwww/qy1GjXc8GMkNSG+TM5W4oArly5RJz5J0EryUyGmGLFi1S58+ftwpZ68VfjByp4uPiVcVKlVyF/0zbdmrTJq0SoGEGe6wdhnhCPvDvFhxcvXq163rQU0G2wGC3pP+zzogkG7GRkpO1imKA98D5HZ7roH7btt11PaTtO7arQ4cOyTruieUDDzSW8/AR30nwSiKjd8sUpCl0VMfoVTPbJr32+mvSKWK2DVzE/TcA+R99/DHWV90dMLBWOP3c26K2sEpgttGIg1UlIzLeyy79zbmhoWEsaeNd2yYZS4WxtHw7dpycdyeR2SuJbArQWCdCwyJEFxYyw97q1IQeMGCAmj59uosQGzdukfMgFdOpETaOnTp1UjVt+rDrHHeC/Vbr3NgGGjVqKOtiZTB23gwJDTo7DKEz9sTZzzl69JgKCNDXg38J7MZa10cDUL/z/v0H1do1a1XHFzpZV/BueB2RQWLol+++N0gKVPRJJpGxTgRZ1X2HDi9wA9AM+dGdFrGxMdZVbgb054aWiQvJ4fC3VAi3+kB8n8Dg0HSSPyQ8Z7ouc5yDRqZ/oFYFTEIHx5+hY8cXJY8x6QWFhKqLly65zoW5De+GD0m6tq39AH4Pb++eBryKyMePH0+vUjDZQGBDYtOwa9GihTjgmAIHAUJC3YQ0gN8C8MILL7iOOfwg/dzENCk2trhrHf4YuKaWkHrffffVVgUKZPS38EtnMUHCB7N8+XK1fMUquXezZo/J/gD4ZFjPb9L2HdtkaYhsEj5e7Md7Xrx48Y5QMbyKyKbAypUrJwWJAjUkNoWbN0++dA0z002NPEbiOZ1usriSnz9L1xyu7fCICNWrZ0/pJrbjs88+k+O4L5b16tZVV69etY5qzJg5i4l9n+tauHdIaIRr26Rq1arK0vTwYT2C72uON2/RUlxFsa57I93d7Mb8h8Zu127dpeHrzfAaIi9ZulT0zSPHjksBZnSRxDr2/7Z9hyzhjWa6bxs00F3LqPYDLAJIkm5mNNgiXH4OSDNmzJDzboWTJ09IHkPkFzv9uY4Kj7xnn3V3gkA6a9s2/JP1/bAeZvuADCZNmizb8+fPV/fee6+sm3c1CfvQpQ54u1T2GiKnUyk42SUUqnnsg78DXCuxnhG9e/eW/drioHv4AnA+X0f7TWhnIzvQMDONQtNI27bNVPf6ns8+95zsB+yWCfs6rhERoWsD/bxuHdtI4vtq1bJyuzFy5Eg5dujQQVd+O5FNLbR08WK1ZMkSbhP8Yp3pffAKIsOxfsoPP6jvpnwvBQcVwS6Nsa9kyZLqu++nyPrKlSvlPDuZTp85rfLmzSfHS5cpq1asWiXrRs99//0PrJz6PHOuvaMEjczx48dLfkPkOrVrqxvXrslxuyUE63IN6xEWLNCdNsYe/MWXI1Tbdu1lHWn8hAk6I8N+HYwkKVAgSk2dpk2IfnzfjO8OX5FTp057tVT2CiKjgE6cOKEqlC8vBQcSmYI0DbOvv/5a5c+XTzVqrDsMDAGxBAGDAnW+4LAIFczqRe5ckcrh8BNShISEyjl/BkOsqtW1Tgsy4QPAEtvofAHsBLyVJQE6sXE5zZEjhwoNDXVt4zrbtm6XfGkpaa4P6czZc3Ls/Y8+0m0Dzov3x3Mj6REsxB/mavV4yxbSIPZGeDyR4dvwHFffO6wqHcQ1JDYSqfLdd6vPh+lGkQGIADIZQnzw4YdyHASEH7O+FjcAbZaHU6dOSV7AnAfYSTl69NeS10hkeKX9FWCIlTnPrY8jEpCW0F06d7Fy6vvZbd0wz4WEhLilOp+D6yCZ8+vW4Zrhxg2vlcoeT2SMTUMBdunSVQrMTmJDptlz5sqyc+fO1lkadgnZhY+Z82G2wzoS9ONQq7EVxDrnzz//rCb/mzFzRl81prIOHTtaR25Gly5d1ODBg1Ut1n+R19/f6rDhdZM0IfW1jDeb+QjNBxQXd0OOj58wURUpUkTWzW+gG476fRIS4lXjxg/KWEVvg0cTGQXZoGEjdfnSZSkoewEKIXkbJBk65HNZt5vBjFQDMJhTn8uEsVw5mzdvrgYP0p0qkIxGd0Vq1NAtZQ2hDKlANuQxPsH/eLq17AcMAQ3gf2GuifxGH+/QoYMEFMyZU39AxpSGBNg/QFMzGJ+MeQsWyhIfQMbfAv7VI0d+pX78Cx59ngaPJvLRo0dVq1atJJAhCso+iNNI4xEjRopFoEXLJ6yzNEAAk5BPzrcK/Msvv7RyscrxwQd8vjaLwQxHDocqXryE+HMAGTXdHTu0Y4+5VkZ/ZENkfFQPP6y7usXsZ0nwdu3aynGDMmX0SBLTYfPVmK+tI/od8DHimvv375fjo0aNUjExkMrpB9DiGNLZs2ekFvM2eDSRIUm//fZb1hE7SiGhwExCnAj46IKUOLZo4UIhnV0iAp98on2WDZEqVKgg++35mjfXvWtIwbaOCwy7B27ciJNGFMj0+uuvcyMRUl1L9po1qqvNmzerw4ePuHRsOPmXLKkJCh3c2IyRDIykXbt2rc4npjSnysPvhOikgJHMJm/p0qVkJDdUFpwDEkuDD0vrw8a9McI74+/g6fBoIqNA9u7bJwWE1rqdyNjXoEEj8akIDg6R/KlceCh8u1TMlw8mNycXtj7HwJDk+vVrvN+h7uYGI66DPKHh7sAtq1atVo8+2ty1jYSOjdy5c7lUBZNCQ4LV7n17VOHCevRKaFhOJpomMeI/Q78FQEzc3zxD7To6NrSxQAzjhiuA48hr3gc1EzzkMKAADlNGGoPM5jcZN268at26dbqGqzfAs0MwMlJTUik4OChd6Cr+PgVly5WjnTu3UcuWLWWbS1zy8XvL9oqVKySypn9gIKUkJ1DNmrVkP4Bh+ciHKJnLly+X4IIjRnwhE6bHXb9CTFLJV7duHfrll59l3d8/iIKCwykh/rrMdxIdXYCcfoEyzB+Ii0+gcqXL0okTxymErxvP+VJTkqlzl860cuVKCfVlng3Pad5p0sQJet2KNfevf/1Llpj3GvvNs9arV5+44YcIBq5QWTiGZIB3wfRp/AFae7wE/AN4JGD77datm+JCFUljpI6WQLoanThxokT8Wb5shZxjJJyRYDDbyblwBGIJZjpKDOzSzuDTTz9V+fPnk/shISqRH0tzo5+bMYK1a9dWgVa0IRxDLxskJPIjXyAnNOa6dutqXVkD9zSqAmDuj6j3uJbxrENILsAcT7HOwegQRM4vD5u6VUvhvkg47x6uWTZu3KRKlCgl+b0FHiuREas4KSmFVq/RksUudcwq4hKzPkn3VKmsd1gweX/66SdZT01NknjH1apVl/0A/zbWsVRZZ8LIftaBqVu37hL8BOn55ztSoL+f1AzJSQk4k0qVKsVS9yTdVUhHBE1OjOfzFUv9RJbUPSk8LISfPYGaNHmQRnwxQvKY++CeRhID2Ac0b95clk7r2Zct0xMCmeNmf+unn6YtW36j1q2f5oO4puzW+Zz+dPHiRQoJDaPIyJz6gJfAY4n8K1f1mBTn2tWr1h4DTbrAwCA6d/48FS1alCIidKHZCYIYxohWyY0g2a5Tpw6FhGgVwBAKf6i+7eSKZ/Vg06ZN8rVgHyIXpTFh0vhjqFixghC9d+/e1LdfX+r9xhv06quvUo0a1UV1Ac6fv0Cp/E0gWvyePXvowMGDsh/3AXCvW6H+/fdTeHg4E15/UFOmTJElnsE8L1Cvbl1Wp3bQXXfpjwj7Xe/DWTBDFkLw5soVKce9BR5HZExNhmkPJk+aRIUKFxKyAm4COCREVv36KNCdFBVdSArQjj/+OEVjx46VdXMIRL4JGc4DNm/ezDrxL0y8AAmv9eOPP1IipP4999A77wykwoUKSWT4Dz/4gCZOGC9R7d98s68rbBXCZOEDYtVCnm/unDmyHx+fka52mH2lS5eh/PkLsJ4PPdlBc+fOlUkeExMT5bhBZM6cxA1UyplTE5U/a/ltnE4kP5kK4sy581S8RHFavHiJvItXgH+obA9YF9C9arpY8dhIiBh0733ahVEP8UHUIG1Z6D+gv2r+2GOim2Yc0NmjxysqZ44cyunUjvfI//aAd6yjGuj6hvN9RsB5CPmN3smEVlxNq/kLFqqmjzwixzKmGjVqqjVr1ir+qJTDz1/OM/eFC2ZG7N33O9/nfWtLY+HCBSooKEC6zwOCdOdMmTJlFNdIVg4NE6hlwkR08jjkfvpe8BvRevL3309R/2jdWtbxex49doz1cm0lMTr3X4Wxrtj1+r8DWR7EEJPAIAI75taAvgrJgyoSQa0xrwamGcD8G6gioSdC2q5dt45efuklOf/gwUPUoMH9rrnqEIgQ+iZrlXTs2DHigpNzDLgBJrrrsGHDafjw4Xz+AanuESSbi4gC/P3pnYED6eSJE9YZXNx872KxsfTee4N4XVfFCGx4+vRpGvLZZ8JGVOlMJHr33cEsSbfLeUwQ0Yvf5fMuX7xAQ4YOZUkdyO8VyGrPA7Rr107R5YFnnnmaqte4l179Zw8KDAqlpMQ42R8QECjTSDCD5D74HUaM/JLmz5srUz/gN8E+1CAzZsxgVSiAUpOTxRoCdWb06NEUHR0tzwyp26zZo3LdSZO+o1de6SFTQmTEt3ytDRs20ugxo2UbQcVLsISOY2n9QseOVLx4cbkeVKhTp/6gxo2bSKByqD/47ZOTk0Q1u3LlCp3mcp3KtRLKtV27dpQ7dx6ZdDJ37txy7dsGEDkrAKtCv3795Kv/dwlWhaiCBcWGyi8v+yAlBg0apMaN+9ZlWTAJEtdIMyayDN+Hw8yA/gNUn7f6uPJ9M/ZbsSpwderaZ09+/v6qVKmSKl++ApwnvX/Df0pOh1NVrVrFde2t27ZZg0EdLDUD1ZdfjpLfYMOGDSq2KAIdakvGNms4P6R4ntx5xOaLEdgIdOjndEisCv7QJA+6ygMDWQK79jklQEtQUDCfm5ulOr8bzvNjScuJ1Qe+jp/rmSZMmCiuq1gvX76CevON3vw76+AtY8aMlrgdWEcsZtjIGzZ8QN3foIGqX7++qlChoipduqwcRwoIDOTfKa8qUaKkqlXzXhUbG6vKlSurqlevoerf30DcALh9wKmieO2VLVdejfrqK/kNbheyjMgNG7kHav5XycGFYZmYbpVAYhAU64cPH1asg6ocERHWXfmFrHwTJ06SpZ9taL4rWU406IWrV6++rGcc5Ywq3HjBpdvP9+Zmkxo9+ivXvi1bNssSRONGlHzEBo880syVb8PGja51BCGva3V03JzS+3aYhOeB6e/dgQNlyJNxBrIn40w/5uuxTORSsr5suTYr9uz5umzDpfXtt9+WdZgGsfyzBNOifRvmQwgObkyn229P+JggjEwP5O1AljX2Or/8kkx/hRmMwkJDZMZ8LgSpBqOioom/aipWvJhM9BJbrDgFBlvz5MECwKoB/1gyGxIaOLA8YE6QIjExMveGadj5c1UXHBQsU+YO/2I4qwOfyn4gOSVFGmMmrrAdrDywwPOjhQsXylzXsFyo1PTzg3BtzvdBuaQHng0HJ02azFWtnhfk8uUrlDdfXhzlZ7lBvXr2lDjNmGB98+ZNkidXZCSrVrphxlKQft2yRaptByaouQlpYqrLCLx3XFwCbeIGZwqrOmi8ZQRm+gcw0Q1UAGDmjJ9owcJFfM/fZBtAg1ADwxEDdeIGbGBgCBUsWMCa+8/Baoy2tugGo5+8AxqMSUk3P59BoULRxDo+q2LuWNNZjSzVkQ8dOiS68alTp6XQfv55JhM0XCYohMUBU3WFhYXJjwSdCz/Q2rVrmSQThSjQnY8dO07zWF9cvnyFkGv2bLTy8f2l0cFDB6n5o4/Rzl275H6AkwsDVgvoyMOGfS7P0K5tW6p1771yDwTNHj1mDOvV0F21KQuFWLBAPurWrat8QJhK7OXOnZkUyfTxxx/Lc0AHzJEjgkaOHEnbtu8QnZalH+u68dShQweqUrUKde/WXa4HlC9fnvbzPZIsQmLeZ+jomOeO1SPWreNlP8jTs+drTDr9gYKAsITs37+fevXqKTppMBNi2k8z+aPbIx+gSmOyOvARpVH79u0oumAUpaalykf97nvvyXXHjZ/EOnJ3unr1CnNVcZskr5gKgTH8/hs3bqJvvvma3z2Y2xSarACmTNu3b6/oyN99/y9atHABDR48WNo10NVRRrjm7t17pI1ylst34aJF1KJFC/4AClLZsuX43cvdtkkwXQCRbwdgOcCIY/jC/qeWccYqKS4uTvYhpbda7BadzmxDrzRWiz59+kjcNaxDBbHDTLeLqhoWBKw/8UT68K9wo3znnfSWDKBHjx6SH/M3Q8WBr0Qg65BwjTTHMqb27dvLhDtQC6Db+vvD4T0Mxl1WjfJbV3Zjy5YtMh7PjqlTp4n+jXuaAbIYQQLrih1Fi8bKsfEToFqx3s3Phxgc2IdzscS1Hn2shazj98QIazhAIZ0/f866kkZSBotPRqAs/26LBXDbiJxZeJN1S5Diqdat1XLW/R5voQsEurNJKEA0PNBgRKMkI2BWM/qiccRB1KG/go2WngvvOhM+AIlb+urzzz9Xw4d9IYR+pk0b1a17d943VDzwypZ1N57gPG8+ILiJ/hVcv36dSVdY2hD4ACK4UcWq0U0f/WhuZOG6P/6ox/Dh98A7wikK+j2rY+JRh0Y0nqtVq/TurZ4KjyOyAcLEjh07VrV/9llXgUmhMUHMKAv4WhQqVMg6I730wBJ5jJRq39492tmOW9UemICcq1luSPqrdu3aqaKxOjYG/BzgT/zCi524MddHPdfheRnubwKFY+zfs3wfaSgxGWvWrHVTwETcz9wzLTVNpaRq/5BNmzaLlcO8W5s2Nr9l2yP+OHWqWA4mTJgg+dD4wzvKh8ONQ0zbgJHeqJUA43/i6fC4nj0DNBIXL17KDcco2eZ3kQTA5gk4HU46efKkJADHoYdiCX2vGDci4X0GnRs2W9iNTT5zLZOfySXbQKlSZWQ7lRuI0F+57pYGHOzl0O3HfjOWhg4ZQuPHjaOJEyfRiRMn+DoB0jVduXIlfW1ONWvVcOmSsBfb7yHgNi3eAZgzZ7Z4tpmu7DZtnpGlgPOZ550y5Qe6l9sD69atl20DvAffgPJwoxTPikkwAXM9T4fHEjkmJoYiIkKpdu3asm0KEjCEgDUjV67c4oIJYL8hJtDsUXQeKPLnxhzrhi4LA0hl8mCJc0B84Bsm6YAB/Vm4BXBDKYiJOpGOHT0mFoyg4FDpqGB5T+EREThbGp9cU8j81al8/w8/eF8Izbo1/TBlCr315ptyXRAK98AzmnvjvkJABhqAgLFmVK/udnCyY84vv1ClihVp0aLFfAF9Pfc7p1HePPno8qWL/DF6xxzULvALeiyqVa+uft+/X/Q+h1NXnybh1V544UVxWH/s0UclPxeqJFOdrlq9WjoT0PhCfqgAgD0P1g0mTBgvHRLIG5AhCCGSbvzhWk41c8YMFRwaIrZe0wC1J8yvZ9Z79eol1/99/wG5n1F/zL1NiC9M1oMlgszYYfJdv6EHoX7++XAVyWoOVAnze+i2A6nu3f/JevlHavbs2XKOt8CjiQxrwJZff5VOESnoDIVWs2ZN9drrr7Ne6bTO0IVu15PLlEEjzOFq9HGV7Dpm8vXs2VsajyEhOo89RMDSJUtlgCksEmYfOmHKcePODC9CQu8cetowLURR6e1DpM4crs4f+GPkz59fnT13Tp7RJMAdKkDff/r09CG7zHN++tln0js6mBuQ0OHNb2G3dMybN18ifyJYojfBo4mMMXuYFQnD/FFI6A2TQuPCg0NQeHio+vrrsXJs5s+z5BxDDoOvv9HHjdS8+54q1hF33uc76DGBSEG2qJ2Ykgy4EXdDAsTA0adt27Z8THdJI6HnsG/fvmojN9ZYB5f8GGaEAa04bg9eiK5nA3NvTIuGY2bYFOu4NzlBmbzoyWvcpLHqaguNYBp6Tqf+qBCdE2McvQ0eTeQzZ84wSfqp3bt3SyHZJbKRsIMGDVY1Wdphil4AhW4SCAFpFm6NwTPD7l999VXJC7z33iCZnhetfwQMh6260F2F1LYdO+S4IZGBCVdruophvbDnMSQEqWvVqsl5MOm5jpkMqf3II4/IcWDXzl0ql/if6Dy43vqNm+SYlsFuqwOmJMbxOXPnq7AwDGh1jw7BEsfgK7Fj586b4nt4AzyayEB9lnhXr15zORyh0EwCATC41EytgPk2ABQ+CGwItoHJgePQZ+3mrbfffkfWkYJtkhgpIV7bb3EduZ5FLaPPGvL07OUOfIi72Un93HNuSY8Bq8bvAVGF+vXry4TU5JUPiJfly1eU8+TZZc19vX/84x8qMjKXmjBRm92ghrh+C+u6kydPUm/yh2VqEm+CxxMZHRsIHGgClLgKjxOkEfbBi65UqVIy7g3IKEWB7t2tHjtpxOmh/EiQbBLPwtpeumSJ2GBRGwCuRqFFLTOpjrn3W1xjAEaPtWPp0mUy5rBEiRKS1+7ZZxIkO8ILFCyYXyaYBMw9zRJD/JF31i9zLS8892yuOlq+Q2kLnfdGsPd4IqPbu169etK9iuH62krgJjN6wmBpmDRRxxM+duyonAcS2Av0IEvrggULckONG1/oPeO8kM6BIVoqhnPDDHErDMy5WNqvs3KlruKNFOzZ63XrSPoPyE5sPEvlynqWKenAsD4CQ0ist2/vDvRiahNzPTjYwxXzQyt+nXl3USus5+jwwvNq2fJVro4Qb4PHExlA1Hn4HEgXrq0gkcyoCAQpjImJVfdYjTmQwE6mqtWqSz6QKCg4WD1ixVE2MyXt3bvPynkzjGScO1fHmIMPhxCJE7aRoBubfICd1AZly5WTGgD5oRs74GtsmxN73z79DDgXPiuAaR8geKLuQXTyO+j7iznQeoYdO3ZKI+/ChQtynrfBK4h89uw58e89duyIFBoK31gvkFC1ovt4zTrdEIPDP5CxujdVfHShaLV4yTJZNyoCZmgyMB+BOd8QFPtNlB8zAQ4mXp83f4Ecs+ezL4HpM/TsUsYPef26da5hSwGBAS4SA/bnhtN7qdJlxa9En6/VE/P+2Fec3+vAgQOiVngrvILIQMWKlaWA8+bJ6ypQU5iGjK2eeEKsEFi/eDH9pOidXnrJdR4sE1hHgwnWAtNhUrV6jXROOrifsXwY7Ny5U/IaEvXr73ZGMvntkhnAOEB40Mk9+Tx5Bl5HMpPlPPTww1ZuN2CJwTEzaSTiOdvf21hhEK4LbQhMa+at8BoiI77ay51fljhsKDwUolStnGCDxRL758yZq0qX1nHXzDS3ILgQwRmQzqPNkBHqhn3Exj+eekrOuxXQ4YE85uOxEzkjfpk9W+YyMdd134M/JGuSSeO5hvWYGPf0afCawz504ERGRso6nteQ2Dx7pUqVJb83S2PAa4gMoLAOHjisevXs5SKBvXDN0KAtv25jKZt+mBCCY4dH5JJ1rafqTg10YdvzmP1IxYoVV9OnT1PXrl+3ngCNvZU6ryVVX3vtNeuIBoZFvf76q65rIKG72+8WQ4fCw/RHhUichuTwQTbHMVyp/XN6egZDXpPIT78fgA8A4xu9GV5FZFgwjOQxhS0tdy5YQ2bsC2Ni7NtjBT/khC5nY6tFMjC+wnOsRpxO/lLdGylpEvyRi8bqrmeQXTvea6mKVLpsGRVmWUBMwjX8bZIeHSQAfJ5hjwYw0BPHUFPYx9e1bdPOZR83jUvznmY+wS+++EJ6EatWrSrX8mZ4FZGBTp06SQR7xC9GYUKa2atb0xUdExujTpw4KetkTXubI4d7EOutMGbMGCu/TlBFtGXCbXcGiTEPCdbtI1BMAnkzDui8q0gRFZ/w51Hkq1WrIfmMPfupVq3Ups1bZB3vBsuMXrrnE4SvNODtKoWB1xEZMIXX8IFGUqhmlidT0Gba3fr1G6qFixbJOpLRZ9EoMw0yLO3WBQDBtMuXd4/4MAkS2BB30aIFluUCU/0yuawGpD0h4Pbp0+mHFuHeuB+WZjLKlJRk1zmdOr2gVqy0vOECuCHKEh0fq5HG5qMCGjduIjr7nQCvJLJ90hfMlYeC1QR2N+RMqlG9urp6xT11gx12awSQ0doQH39D/fLLLJkqNyAAOqlTOlGcTF54s8ESoe3YWuJjtArUlZ07d1tX0LB/OEDGj8fo5Z1efllt2qB7Du0JJMb7YYntDevXq8OHD90x0hjwSiID6ADIqC8j/fDDD7LvPmu2UJNuxF1XDRtqCT558mTJY2CkpFnPSHADcy3jq6wbh3of7NwZYSewua6dwOj0yGFZJEaN/lrNmjlL1uH6GRgcJHmMs5BpyPZ+Q9vI7yQSA15LZOCjjz5WXXt0l3UUMlK85ewz9HM9QY5dSs9bMF/17aujFaGXzFTtgKnubwXsh38vete0G6fbyejRRx9T7w96X2y5Jq8dRvpm3D9ypJ4yAmn7zl2qZcvHZd30FhYuVNTK6X63+g3ul+07jcSAVxL58uXLUphPcKMIBTxx/Hjp/TMFHhGh3TbhAA+90j4vSKsnW0vVb7Yxcfl/gl0tcI3msAj33mB3MEKX9HU5Yd4MM2cIUrcer7gmuUGC+oDnNdeuU6euy4b84EN61AjW8e5It6oFvBVeSeRUJoxp6Zs0Zcq/WPKluLad8P9lQphGkvEJxrHg4EC1es16NWLECFf+1s88c8vonAZGJZhiTSNsGn2v9+wt+/8drly5qr766ivxI8Y5TZs1k/BaL1u9jUioOcyzYmnvuMHQfkBPwqMtIyDyrl27ZP+dAK9VLV7s9KJFAHjEaXsu3DkR7MV0B6NhZiczyBdoTXiD1LhJE/Xj1Glq+owZrn3RUVGsh75xU9AXg4wdIvBI+zPMnDlT5gk0127e4nG1YvlKkaSmIwYfl5HEhsT6g9MNwOef7yDXMjZr+JnggzD+JHcKvI7IkIxwaYREQhUsktGhZzhCQffpowvYzDYqhc/HcNyQBV3asAGb48EhoeKMvnz5cnHAMfuRqlSpwmT9WM2fP08iE4W5et7cPYBPPNFKzZ07R/ymn7XicJgEP2GQbvfePeoN/kDsx3THi7vmMM+IY06nQ33zzbfyLia/yYf1TSzREYAlYwAXb0WWx0e+3Zg6bRo99eSTMjSfyUSJCfGE0A2pMiGSkmH5lSpVoi1bthBLM/ruu+8lJhwTRIbj4+dAwvB888NgDhADxFDOX6CAxFnef+AAbd36G61ds0ZiBQMI3If4cWlpqcRVPbFeTDdu3JBjwD33VCH+0KgCP0O+PLnp+tVrNGvObJo/b54c90PoACssAJIJQ4BQAml8LQzpR0DHufPn0uk/Tsv0E4DD6S/PzbUNIXx0ShJi0j0nsY2ffvppyePN8Doic+tfArQESKDABCZVEPXo8QoNHTqECZtM8xcsoFOnTtGA/v1p1649TOjN1K5tGzphBXFB8BJEndS/iv5pQGoAAQsNQNbixYvJlGb169ejqKhClJiYQKHh4RQfF88fUBJfCxPyhJF/gJOuX4sjFqK0ccMGWrFqHe3dvYPOnD1rXQ1EDKCAAE1GEFj2Ia4F/yVbETHDwkKpbr37ad7cORLssWPHjhJRFLE9jh8/LnkcDn5+P0xZxh8RnzebP5JHmj4ix7waILI34NKlS+IiCZUC7ox4NcQlQYMHvVvJ3ADs16+/TDAJwEaLvKtXr+LqN149+eTTqmBBHVYACVW4vVqXhE4HS181+f5fyekvtmaoNunuw0lUIes9EPAb/hKzZ/8iZr4KFSuxuuKO2QbVBP7Gv8yaJe9krm8afRhpjpjNf2Y+9AZ4BZFRuKYAWYpJ0O6SJUvI8Kf16zdYuW6NV155RbVt206tXbtarVu3Tj322GMSBd+QQRPi1kQTYgfqOfSw7o9lgNaRjXcd8omubuUxSfZzwkdhv67p2ECCFQLz5Y0cMUIdPHRYombiPc14wVvh00+HqBYtmkuDEXnt1g0Q2lvhFUT+6KOPhMCmhV+Npdd/M0XttWvXZHTIqFFfigSfO3eekKFEieLSqDJEQBe0nXRIGUkOba1c+bLSsGvQoCFLVT25+Z8lY5Fw34NU3jx5VPXq1VWv3m/KEK4xo8dImACYA/8b6Kj0Dmm84rogtrfC43VkTK/VtWtXOnXmHDeGEAgwWQJZv/jii1aOvw5MF9a8eQsJ7p0zRw66dv06bdu2lVatWkWHDh6kI0ePWjktsB5q9Gk/0UtZn2W9/MsvR1Hnzi/Tb9ygrFKtmsR+Qx6ldFw30YGVno7XAEHEY2OLUt26dal0qdJUrXp12rFzOzck11KR2Fh6e8AAV8PvrwJBuTFpJhAYHEYF8uWmGjVqSBy5//Za2R0eTeTJkydTnz59uKGDhlqaTPXQuXNneuihh2Teu/8VmL+uX7++9OCDD1N4eCgVLhxDly5dFEJjlqYLFy5IhE9E2bwV7oqJob78XJhVavfu3dbe9EDDrUiRWJnmIDQknO5vUJ8qVqyoP4bkFJo69Ue6p0oVmd0Kloj/BYgOinn9Vq1cSfPmz+cKxZ/CQoJlEksEYkQkfm+BRxIZBfRKjx40iYkMREbmoipc6EOGfEaVK6efrvf/g+sskUeNGkWzZ8+mvn37MoEvUkyRGDp39pxMibZ//+/k5+9PV69cocuXL7nm18DxEyePy/wnmDASM6rCLIfnxGTnIGZk7kgqXaIURReK5mPBtH79epmKYj4TDtOpITRsZgHmxZf4g1iyZIlYN8qWLSsTzXP7QKYRM1YZT4bHERnmq1ZPPikFAttpzhxh1Lv3GzKPB+awyCpgTo+tW7exRN4vE/qsWbOGSpYsTcGhweTn0GY2mfs6MYGJEUCJifHkx2pHIBMY85YAcXFxlMzrMNNdvnSFKleqKBIecw3iA0TCnCpZhWeffZYmTZok63fdFSMxpju91IkaNmggH5gnw6OIvHz5cikMkFg6MBxpNH78BAmeXb58BStX1gM/2dmzZ2kdS9FjrDdD54VU++KLEXTlyiWKLVqc96VKkG5M13uZJfY/X+khZEHnCJZVqlal8uXKyeQ3twt4zjJlytDBQ0dkwiDgYVbDnmnThpo1a8Y1hgfPTw0iewIw1QJa3WLf5dY+Hh2mMpiksgN27NihuLEmz5UxYUT3t9/q7uS/G8ZPG6NXzNi+hx9uqr7//nt1/vx5K5fnwSOIrCeCAYkDxFyFHx8jMGbMmJlt4vwOHPiuPBcIom3LuqND7MX+/jKSmSWilfvvxcyZM2y/pzbN1ahRQyaS9FR4BJFlOjFxUdRuilwlq1WrVqujR3Uct+wA+BojRrJIO64xQBLEnHu85RNq//4DMjF8dsLECRPlWR1OzLKqyfzpp596bKTObE9kuF3iB8cPnSdPbhVTJFZxg+W/6vC4nRBJx1IYczwHhwSrPn37WkeyHzDa3Py2SOHh4apxk4eso56FbG8VNw40QJky5Wn6T9OoefPmWWqh+F/Bv6csU2XqXAelpqZRrZo1xYyXHdGkSRPimo1YEosNHs/Z+IGG1lHPgkdYLQ4fPiLTxoK8sMtmZ2AGqUGDBtH0GTPI6XAQNwKpXLly1tHsC1hhYAMHoT2x188jnrho0VixsWZ3EgPoUbz77soQz1KbmHkAszvy588vfs6e2nXtXR3u2QCo4E4cd3ddHzx40FrzISvh0b4W2RGQyPCvSGMdGT16RWLukl5Bb+gGzs7wSeRMxieffCK+DejVU2mp4oXnI3HWwyeRMxn4OaFrnjx5hnXkJPGkg1ebD1kLn0TOZMANM1euSCax9mWAd5kPWQ8fkTMZkMjabqzECScrvdl8cMNH5EwGRnEfOnSY15wimRMS9AhoH7IWPiJnMuD0r6EoPDxCOkh8yHr4iJzJOHLkiCzh9J+QEJ9tu6e9DT4iZzJWrlwpS6efg86fPyfrdn8RH7IGPiJnMuCzAKg0Rdeux9HixUt8evJtgI/ImYxrV6/KEj4LKjWNrl7T2z5kLXxEzmQ0fOABWSJ4ocPpoDq1a8sIah+yFj4iZzJq1qghSwz5L1Agn4yc9nVRZz18RM5kFCpUSHx6kxKTKCkpjXLnzmMd8SEr4SNyJgO68cCBA5nM0RQcHEitWj1hHfEhK+FzGsoiIJB46dKlKTw83NrjQ1bCR2QfvABE/weeHOtqBE4RXQAAAABJRU5ErkJggg==" />',
            TACHO : '<image width="178" height="128" alt="tacho" xlink:href="data:image/png;base64,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" />',
            CHECKMARK : '<image width="178" height="128" alt="checkmark" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAYAAACvODPOAAAABHNCSVQICAgIfAhkiAAAAAFzUkdCAK7OHOkAAAAEZ0FNQQAAsY8L/GEFAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAFKElEQVR4Xu3dy0okSxCA4VREUQRFQRAUXOnG53Drxo2P5wO48UVcCm7EK4LiZSGIF+ac6JNzRrun7a6qvERG/h8MVrmSit80WsWZ+PUvBxRu0r8FikbIMIGQYQIhwwRChgmEDBMIGcEcHBy49fV1t7297SYmJnr/UuH7yAhieXnZPTw8+LvvUiRGyOhkamrKfX5++rvhYmfGaoHWJOBxIhax1wxCRisfHx9ubm7O340nZsysFmhMIp6dne29bSNGcoSMRkKdqqGzY7XA2EKuBqHXDELGWKKsAwFjJmSM9Pb25iYn46QSKmZCxlAvLy+90GZmZvx74ggRMy/2MFToPXaULilyIuOvXl9f/VU6XT5xOJEx4P393U1PT/u79NokyYmMb+SHHDkjFm1OZk5k/C/1TjxKkzQ5kdHz9PTkr/Lb3NzsvW3yicWJDPf4+OiWlpb8XX6y2sj3rpukSciVk1+Gl1+K14iQMRZtO/FXTbNkR66U/NTOEkKu0PPzs5ufn/d3+rRZEgi5Mvf3925xcdHf6dN202VHrojmnVh0SZETuRI3Nzf+Sqeu5ykncgVub2/d6uqqv9MnRIKEbNzd3Z1bWVnxd/qEyo+QDbO8E/djRzbq+vraX+kU+vwkZIPOz8/d2tqav9MnxhJAyMacnZ25jY0Nf6dPrE2WHdmQmnbifpzIRlxcXPgrnWKfl5zIBlxdXfX+wLZWKRLjRC7c5eVl9RELTuSC1bwT91MZcv+A+FwbRMTfqVstfg9oYWGh91bI+7QPLiUiHqQq5K8Dkl/+7kfMzp2cnPgrnXJ99VSzWjSJVMmHnBwn8XAqTuSmA6rxZD49PfVXOuU+XLKH3DbK3d1df2WfPKOtrS1/p4+Gr5BZQ+5ysh4dHbm9vT1/Z5f2rz5a1rxsIYcY0OHhodvf3/d39hDx+LK82IsxIE0PNQQibib5iRxrQNoH3wQRN5c05NgDshAzEbeTLORUAyo5ZiJuL0nIqQdUYsxE3E30kHMNqKSYibi7qCHnHlAJMRNxGElf7OWgORQiDsd8yEJjMEQcVtSQNf0NXk3hEHF4UUPe2dnxVzpoCIiI44j6I2qtQ8s1LCKOp4oduV+OoIg4ripDFinDIuL4ooas/QGlCIyI04h+ItccMxGnk2S1qDFmIk4r2Y5cU8xEnF7SF3s1xEzEeSQNWViOmYjzSR6ysBgzEeeVJWRhKWYizi9byKKEmEdFSsQ6ZA1ZlPCgh8VKxHpkD1mUFLN8rMfHx0SsjKo/9K09jlLUFrFQcSL/VuMAQqv1GaoKWRBzezU/O3UhC2JurvZnpjJkQczj41kpDlkwoNF4Rv9RHbJgUMPxbP5QH7JgYIN4Jt8VEbJgcH/wLAYVE7JggDyDYYoKWdQ8SCIerriQRY0DJeKfFRmyqGmwRDxasSGLGgZMxOMpOmRhedBEPL7iQxYWB07EzZgIWVgaPBE3ZyZkYSEAIm7HVMii5BCIuD1zIYsSgyDibkyGLEoKg4i7MxuyKCEQIg7DdMhCcyhEHI75kIXGYIg4rCpCFprCIeLwqglZaAiIiOOoKmSRMyQijqe6kEWOoIg4ripDFinDIuL4qg1ZpAiMiNOoOmQRMzQiTqf6kEWM4Ig4LUL2QoZHxOkR8hchAiTiPAi5T5cQiTgfQv6LNkEScV6EPESTMIk4P0L+wTiBErEOhDzCT6ESsR6q/nsyoC1OZJhAyDCBkGECIcMEQoYJhAwTCBkmEDJMIGSYQMgwgZBhAiHDAOf+Ae9fkLQ16eSQAAAAAElFTkSuQmCC" />'
        };
        this.buttons = {
            escape : false,
            up : false,
            left : false,
            enter : false,
            right : false,
            down : false
        };
        this.frontLeft = {
            x : 22.5,
            y : -25,
            rx : 0,
            ry : 0,
            bumped : false
        };
        this.frontRight = {
            x : -22.5,
            y : -25,
            rx : 0,
            ry : 0,
            bumped : false
        };
        this.backLeft = {
            x : 20,
            y : 30,
            rx : 0,
            ry : 0,
            bumped : false
        };
        this.backRight = {
            x : -20,
            y : 30,
            rx : 0,
            ry : 0,
            bumped : false
        };
        this.backMiddle = {
            x : 0,
            y : 30,
            rx : 0,
            ry : 0
        };
        this.time = 0;
        this.timer = {
            timer1 : false,
            timer2 : false,
            timer3 : false,
            timer4 : false,
            timer5 : false
        };
        var SpeechSynthesis = window.speechSynthesis;
        if (!SpeechSynthesis) {
            context = null;
            console.log("Sorry, but the Speech Synthesis API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox");
        }
        this.sayText = {
            language : "en-US",
            say : function(text, lang, speed, pitch) {
                // IE apparently doesnt support default parameters, this prevents it from crashing the whole simulation
                speed = (speed === undefined) ? 30 : speed;
                pitch = (pitch === undefined) ? 50 : pitch;
                // Clamp values
                speed = Math.max(0, Math.min(100, speed));
                pitch = Math.max(0, Math.min(100, pitch));
                // Convert to SpeechSynthesis values
                speed = speed * 0.015 + 0.5; // use range 0.5 - 2; range should be 0.1 - 10, but some voices dont accept values beyond 2
                pitch = pitch * 0.02 + 0.001; // use range 0.0 - 2.0; + 0.001 as some voices dont accept 0

                var utterThis = new SpeechSynthesisUtterance(text);
                if (lang === "") {
                    console.log("Language is not supported!");
                } else {
                    var voices = SpeechSynthesis.getVoices();
                    for (var i = 0; i < voices.length; i++) {
                        if (voices[i].lang.indexOf(lang) !== -1 || voices[i].lang.indexOf(lang.substr(0, 2)) !== -1) {
                            utterThis.voice = voices[i];
                            break;
                        }
                    }
                    if (utterThis.voice === null) {
                        console.log("Language \"" + lang
                                + "\" could not be found. Try a different browser or for chromium add the command line flag \"--enable-speech-dispatcher\".");
                    }
                }
                utterThis.pitch = pitch;
                utterThis.rate = speed;
                utterThis.onend = function(event) {
                    that.sayText.finished = true;
                }
                SpeechSynthesis.speak(utterThis);
            },
            finished : false
        };
        this.tone = {
            duration : 0,
            timer : 0,
            file : {
                0 : function(a) {
                    var ts = a.context.currentTime;
                    a.oscillator.frequency.setValueAtTime(600, ts);
                    a.gainNode.gain.setValueAtTime(a.volume, ts);
                    ts += 1;
                    a.gainNode.gain.setValueAtTime(0, ts);
                },
                1 : function(a) {
                    var ts = a.context.currentTime;
                    for (var i = 0; i < 2; i++) {
                        a.oscillator.frequency.setValueAtTime(600, ts);
                        a.gainNode.gain.setValueAtTime(a.volume, ts);
                        ts += (150 / 1000.0);
                        a.gainNode.gain.setValueAtTime(0, ts);
                        ts += (25 / 1000.0);
                    }
                },
                2 : function(a) {
                    var C2 = 523;
                    var ts = a.context.currentTime;
                    for (var i = 4; i < 8; i++) {
                        a.oscillator.frequency.setValueAtTime(C2 * i / 4, ts);
                        a.gainNode.gain.setValueAtTime(a.volume, ts);
                        ts += (100 / 1000.0);
                        a.gainNode.gain.setValueAtTime(0, ts);
                        ts += (25 / 1000.0);
                    }
                },
                3 : function(a) {
                    var C2 = 523;
                    var ts = a.context.currentTime;
                    for (var i = 7; i >= 4; i--) {
                        a.oscillator.frequency.setValueAtTime(C2 * i / 4, ts);
                        a.gainNode.gain.setValueAtTime(a.volume, ts);
                        ts += (100 / 1000.0);
                        a.gainNode.gain.setValueAtTime(0, ts);
                        ts += (25 / 1000.0);
                    }
                },
                4 : function(a) {
                    var ts = a.context.currentTime;
                    a.oscillator.frequency.setValueAtTime(100, ts);
                    a.gainNode.gain.setValueAtTime(a.volume, ts);
                    ts += (500 / 1000.0);
                    a.gainNode.gain.setValueAtTime(0, ts);
                }
            }
        };
        this.svg = '<svg id="svg'
                + this.id
                + '" xmlns="http://www.w3.org/2000/svg" width="313" height="482" viewBox="0 0 313 482">'
                + '<path stroke-alignment="inner" d="M1 88h17.5v-87h276v87h17.5v306h-17.5v87h-276v-87h-17.5z" style="fill:#fff;stroke:#000;stroke-width:2"/>'
                + '<rect x="19.5" y="2" width="274" height="225" style="fill:#A3A2A4;stroke:none"/>'
                + '<rect x="19.5" y="202" width="274" height="25" style="fill:#635F61;stroke:none"/>'
                + '<path d="M45 47.4c0-5.3 5.7-7.7 5.7-7.7s206.7 0 211 0c4.3 0 6.7 7.7 6.7 7.7v118.3c0 5.3-5.7 7.7-5.7 7.7s-206.7 0-211 0S44 164.7 44 164.7" fill="#333"/>'
                + '<rect x="67" y="41" width="180" height="130" fill="#ddd"/>'
                + '<line x1="155.7" y1="246" x2="155.7" y2="172.4" style="fill:none;stroke-width:9;stroke:#000"/>'
                + '<path id="led'
                + this.id
                + '" fill="url("#LIGHTGRAY'
                + this.id
                + '") d="M155.5 242.5 l20 0 40 40 0 52 -40 40 -40 0 -40 -40 0 -52 40 -40z" fill="#977" />'
                + '<path id="up'
                + this.id
                + '" class="simKey" d="M156 286c0 0 7 0 14.3-0.2s9 7.2 9 7.2v12.3h10.5v-19.5l9.7-9.7c0 0 2.8-0.2 0-3.3-2.8-3.2-26.5-25.7-26.5-25.7h-17-0.3-17c0 0-23.7 22.5-26.5 25.7s0 3.3 0 3.3l9.7 9.7v19.5h10.5v-12.3c0 0 1.7-7.3 9-7.2s14.3 0.2 14.3 0.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                + '<path id="down'
                + this.id
                + '" class="simKey" d="M156 331c0 0 7 0 14.3 0.2s9-7.2 9-7.2v-12.3h10.5v19.5l9.7 9.7c0 0 2.8 0.2 0 3.3-2.8 3.2-26.5 25.7-26.5 25.7h-17-0.3-17c0 0-23.7-22.5-26.5-25.7s0-3.3 0-3.3l9.7-9.7v-19.5h10.5v12.3c0 0 1.7 7.3 9 7.2s14.3-0.2 14.3-0.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                + '<path id="enter'
                + this.id
                + '" class="simKey" d="M138 293c0-1.4 0.9-2 0.9-2s32.6 0 33.2 0 1.1 2 1.1 2v31.4c0 1.4-0.9 2-0.9 2s-32.5 0-33.2 0c-0.7 0-1-2-1-2V293.1z" style="fill:#3C3C3B;stroke-width:2;stroke:#000"/>'
                + '<path id="escape'
                + this.id
                + '" class="simKey" d="M37 227v26.4c0 0 1.2 4.8 4.9 4.8s44.8 0 44.8 0l15.7-15.6v-15.7z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                + '<path id="left'
                + this.id
                + '" class="simKey" d="M69 309c0 12.5 14 17.9 14 17.9s27.1 0 29.8 0 2.8-1.7 2.8-1.7v-16.4 0.1-16.4c0 0-0.2-1.7-2.8-1.7s-29.7 0-29.7 0S69.3 296.7 69.3 309.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                + '<path id="right'
                + this.id
                + '" class="simKey" d="M242 309c0 12.5-14 17.9-14 17.9s-27.1 0-29.7 0-2.8-1.7-2.8-1.7v-16.4 0.1-16.4c0 0 0.2-1.7 2.8-1.7s29.8 0 29.8 0S241.9 296.7 241.9 309.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>'
                + '<rect x="19" y="412.4" width="274" height="67.7" style="fill:#A3A2A4"/>'
                + '<rect x="2" y="376" width="17.5" height="17.5" style="fill:#635F61"/>'
                + '<rect x="294" y="376" width="17.5" height="17.5" style="fill:#635F61"/>'
                + '<rect x="231.7" y="426.6" width="9.6" height="5.4" style="fill:#E52520;stroke:#000"/>'
                + '<rect x="246.2" y="426.7" width="9.6" height="5.4" style="fill:#E52520;stroke:#000"/>'
                + '<rect x="227.5" y="432.4" width="32.6" height="26.2" style="fill:#E52520;stroke:#000"/>' + '<g id="display' + this.id
                + '" clip-path="url(#clipPath)" fill="#000" transform="translate(67, 41)" font-family="Courier New" font-size="10pt">' + '</g>' + '<defs>'
                + '<clipPath id="clipPath">' + '<rect x="0" y="0" width="178" height="128"/>' + '</clipPath>' + '<radialGradient id="ORANGE' + this.id
                + '" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">' + '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />'
                + '<stop offset="100%" style="stop-color:rgb( 255, 165, 0);stop-opacity:1" />' + '</radialGradient>' + '<radialGradient id="RED' + this.id
                + '" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">' + '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />'
                + '<stop offset="100%" style="stop-color:rgb(255,0,0);stop-opacity:1" />' + '</radialGradient>' + '<radialGradient id="GREEN' + this.id
                + '" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">' + '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />'
                + '<stop offset="100%" style="stop-color:rgb(0,128,0);stop-opacity:1" />' + '</radialGradient>' + '<radialGradient id="LIGHTGRAY' + this.id
                + '" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">' + '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />'
                + '<stop offset="100%" style="stop-color:rgb(211,211,211);stop-opacity:1" />' + '</radialGradient>' + '</defs>' + '</svg>';
        $("#simRobotContent").append(this.svg);
        $("#svg" + this.id).hide();
    }

    Ev3.prototype = Object.create(Robot.prototype);
    Ev3.prototype.constructor = Ev3;

    Ev3.prototype.reset = function() {
        this.encoder.left = 0;
        this.encoder.right = 0;
        this.left = 0;
        this.right = 0;
        this.led.color = "LIGHTGRAY";
        this.led.mode = C.OFF;
        this.led.blink = 0;
        // Ev3.time = 0;
        for ( var key in this.timer) {
            this.timer[key] = 0;
        }
        var that = this;
        for ( var property in that.buttons) {
            $('#' + property + that.id).off('mousedown touchstart');
            $('#' + property + that.id).on('mousedown touchstart', function() {
                that.buttons[this.id.replace(/\d+$/, "")] = true;
            });
            $('#' + property + that.id).off('mouseup touchend');
            $('#' + property + that.id).on('mouseup touchend', function() {
                that.buttons[this.id.replace(/\d+$/, "")] = false;
            });
        }
        $("#display" + this.id).html('');
        this.tone.duration = 0;
        this.tone.frequency = 0;
        this.webAudio.volume = 0.5;
    };

    /**
     * Update all actions of the Ev3. The new pose is calculated with the
     * forward kinematics equations for a differential drive Ev3.
     * 
     * @param {actions}
     *            actions from the executing program: power for left and right
     *            motors/wheels, display, led ...
     * 
     */
    Ev3.prototype.update = function() {
        // update pose
        var motors = this.robotBehaviour.getActionState("motors", true);
        if (motors) {
            var left = motors.c;
            if (left !== undefined) {
                if (left > 100) {
                    left = 100;
                } else if (left < -100) {
                    left = -100;
                }
                this.left = left * C.MAXPOWER;
            }
            var right = motors.b;
            if (right !== undefined) {
                if (right > 100) {
                    right = 100;
                } else if (right < -100) {
                    right = -100;
                }
                this.right = right * C.MAXPOWER;
            }
        }
        var tempRight = this.right;
        var tempLeft = this.left;
        this.pose.theta = (this.pose.theta + 2 * Math.PI) % (2 * Math.PI);
        this.encoder.left += this.left * SIM.getDt();
        this.encoder.right += this.right * SIM.getDt();
        var encoder = this.robotBehaviour.getActionState("encoder", true);
        if (encoder) {
            if (encoder.leftReset) {
                this.encoder.left = 0;
            }
            if (encoder.rightReset) {
                this.encoder.right = 0;
            }
        }
        if (this.frontLeft.bumped && this.left > 0) {
            tempLeft *= -1;
        }
        if (this.backLeft.bumped && this.left < 0) {
            tempLeft *= -1;
        }
        if (this.frontRight.bumped && this.right > 0) {
            tempRight *= -1;
        }
        if (this.backRight.bumped && this.right < 0) {
            tempRight *= -1;
        }
        if (tempRight == tempLeft) {
            var moveXY = tempRight * SIM.getDt();
            var mX = Math.cos(this.pose.theta) * moveXY;
            var mY = Math.sqrt(Math.pow(moveXY, 2) - Math.pow(mX, 2));
            this.pose.x += mX;
            if (moveXY >= 0) {
                if (this.pose.theta < Math.PI) {
                    this.pose.y += mY;
                } else {
                    this.pose.y -= mY;
                }
            } else {
                if (this.pose.theta > Math.PI) {
                    this.pose.y += mY;
                } else {
                    this.pose.y -= mY;
                }
            }
            this.pose.thetaDiff = 0;
        } else {
            var R = C.TRACKWIDTH / 2 * ((tempLeft + tempRight) / (tempLeft - tempRight));
            var rot = (tempLeft - tempRight) / C.TRACKWIDTH;
            var iccX = this.pose.x - (R * Math.sin(this.pose.theta));
            var iccY = this.pose.y + (R * Math.cos(this.pose.theta));
            this.pose.x = (Math.cos(rot * SIM.getDt()) * (this.pose.x - iccX) - Math.sin(rot * SIM.getDt()) * (this.pose.y - iccY)) + iccX;
            this.pose.y = (Math.sin(rot * SIM.getDt()) * (this.pose.x - iccX) + Math.cos(rot * SIM.getDt()) * (this.pose.y - iccY)) + iccY;
            this.pose.thetaDiff = rot * SIM.getDt();
            this.pose.theta = this.pose.theta + this.pose.thetaDiff;
        }
        var sin = Math.sin(this.pose.theta);
        var cos = Math.cos(this.pose.theta);
        this.frontRight = this.translate(sin, cos, this.frontRight);
        this.frontLeft = this.translate(sin, cos, this.frontLeft);
        this.backRight = this.translate(sin, cos, this.backRight);
        this.backLeft = this.translate(sin, cos, this.backLeft);
        this.backMiddle = this.translate(sin, cos, this.backMiddle);

        this.touchSensor = this.translate(sin, cos, this.touchSensor);
        this.colorSensor = this.translate(sin, cos, this.colorSensor);
        this.ultraSensor = this.translate(sin, cos, this.ultraSensor);
        this.mouse = this.translate(sin, cos, this.mouse);

        this.touchSensor.x1 = this.frontRight.rx;
        this.touchSensor.y1 = this.frontRight.ry;
        this.touchSensor.x2 = this.frontLeft.rx;
        this.touchSensor.y2 = this.frontLeft.ry;

        //update led(s)
        var led = this.robotBehaviour.getActionState("led", true);
        if (led) {
            var color = led.color;
            var mode = led.mode;
            if (color) {
                this.led.color = color.toUpperCase();
                this.led.blinkColor = color.toUpperCase();
            }
            switch (mode) {
            case C.OFF:
                this.led.timer = 0;
                this.led.blink = 0;
                this.led.color = 'LIGHTGRAY';
                break;
            case C.ON:
                this.led.timer = 0;
                this.led.blink = 0;
                break;
            case C.FLASH:
                this.led.blink = 2;
                break;
            case C.DOUBLE_FLASH:
                this.led.blink = 4;
                break;
            }
        }
        if (this.led.blink > 0) {
            if (this.led.timer > 0.5 && this.led.blink == 2) {
                this.led.color = this.led.blinkColor;
            } else if (this.led.blink == 4 && (this.led.timer > 0.5 && this.led.timer < 0.67 || this.led.timer > 0.83)) {
                this.led.color = this.led.blinkColor;
            } else {
                this.led.color = 'LIGHTGRAY';
            }
            this.led.timer += SIM.getDt();
            if (this.led.timer > 1.0) {
                this.led.timer = 0;
            }
        }
        $("#led" + this.id).attr("fill", "url('#" + this.led.color + this.id + "')");
        // update display
        var display = this.robotBehaviour.getActionState("display", true);
        if (display) {
            var text = display.text;
            var x = display.x;
            var y = display.y;
            if (text) {
                $("#display" + this.id).html($("#display" + this.id).html() + '<text x=' + x * 10 + ' y=' + (y + 1) * 16 + '>' + text + '</text>');
            }
            if (display.picture) {
                $("#display" + this.id).html(this.display[display.picture]);
            }
            if (display.clear) {
                $("#display" + this.id).html('');
            }
        }
        // update tone
        var volume = this.robotBehaviour.getActionState("volume", true);
        if (volume && this.webAudio.context) {
            this.webAudio.volume = volume / 100.0;
        }
        var tone = this.robotBehaviour.getActionState("tone", true);
        if (tone && this.webAudio.context) {
            var ts = this.webAudio.context.currentTime;
            if (tone.frequency) {
                this.webAudio.oscillator.frequency.setValueAtTime(tone.frequency, ts);
                this.webAudio.gainNode.gain.setValueAtTime(this.webAudio.volume, ts);
            }
            if (tone.duration) {
                ts += tone.duration / 1000.0;
                this.webAudio.gainNode.gain.setValueAtTime(0, ts);
            }
            if (tone.file !== undefined) {
                this.tone.file[tone.file](this.webAudio);
            }
        }
        // update sayText
        this.sayText.language = GUISTATE_C.getLanguage(); // reset language
        var language = this.robotBehaviour.getActionState("language", true);
        if (language !== null && language !== undefined && window.speechSynthesis) {
            this.sayText.language = language;
        }
        var sayText = this.robotBehaviour.getActionState("sayText", true);
        if (sayText && window.speechSynthesis) {
            if (sayText.text) {
                this.sayText.say(sayText.text, this.sayText.language, sayText.speed, sayText.pitch);
            }
        }
        // update timer
        var timer = this.robotBehaviour.getActionState("timer", false);
        if (timer) {
            for ( var key in timer) {
                if (timer[key] == 'reset') {
                    this.timer[key] = 0;
                }
            }
        }
    };
    /**
     * Translate a position to the global coordinate system
     * 
     * @param {Number}
     *            sin the sine from the orientation from the Ev3
     * @param {Number}
     *            cos the cosine from the orientation from the Ev3
     * @param {point}
     *            point to translate
     * @returns the translated point
     * 
     */
    Ev3.prototype.translate = function(sin, cos, point) {
        point.rx = this.pose.x - point.y * cos + point.x * sin;
        point.ry = this.pose.y - point.y * sin - point.x * cos;
        return point;
    };

    return Ev3;
});