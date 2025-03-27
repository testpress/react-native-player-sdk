export type TpStreamsPlayerProps = {
  videoId: string;
  accessToken: string;
  enableDownload?: boolean;
  autoPlay?: boolean;
  startAt?: number;
  offlineLicenseExpireTime?: number;
  style?: import('react-native').ViewStyle;
};
