package statsclient

import net.miginfocom.swing.MigLayout;

application(title: app.config.application.title,
  preferredSize: [320, 240],
  layout: new MigLayout(),
  pack: true,
  //location: [50,50],
  locationByPlatform:true,
  iconImage: imageIcon('/griffon-icon-48x48.png').image,
  iconImages: [imageIcon('/griffon-icon-48x48.png').image,
               imageIcon('/griffon-icon-32x32.png').image,
               imageIcon('/griffon-icon-16x16.png').image]) {

    button("Open Stats", actionPerformed:controller.openStatsWindow)
}
